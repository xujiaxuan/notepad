/*
 * Copyright (C) 2013, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * File name: FileBrowser.java
 * 
 * Description: the FileBrowser activity, we can choose the file and return the path of the file
 *  
 * Author: fuping
 * 
 * Ver 1.0, 2013-5-3, fuping, Create file
 */

package com.tplink.android;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


/**
 * The class FileBrowser
 */
public class FileBrowser extends ListActivity {

    private TextView mPath;

    private List<String> items = null;

    private List<String> paths = null;

    private String rootPath = "/";

    public void onCreate(Bundle saveInstanceState) {
        super.onCreate(saveInstanceState);
        setContentView(R.layout.file_browser);

        mPath = (TextView)findViewById(R.id.file_path_current);
        getFileDir(rootPath);
    }

    /**
     * get the file's directory, set the sub directory of the file to
     * listAdapter
     */
    private void getFileDir(String filePath) {
        mPath.setText(filePath);

        items = new ArrayList<String>();
        paths = new ArrayList<String>();

        File f = new File(filePath);
        File[] files = f.listFiles();

        /*
         * if the filePath is not the root
         */
        if (!filePath.equals(rootPath)) {
            items.add("Back to" + rootPath);
            paths.add(rootPath);

            items.add("Back to ../");
            paths.add(f.getParent());
        }

        for (int i = 0; i < files.length; i++) {
            items.add(files[i].getName());
            paths.add(files[i].getPath());
        }

        ArrayAdapter<String> fileList = new ArrayAdapter<String>(this, R.layout.file_row, items);
        setListAdapter(fileList);
    }

    /**
     * corresponding to the click of the item, check the type of the file, if
     * the file is .txt file, return the path of the file to the Notepad
     * activity
     */
    protected void onListItemClick(ListView l, View v, int position, long id) {
        File file = new File(paths.get(position));

        if (file.canRead()) {
            if (file.isDirectory()) {
                getFileDir(paths.get(position));
            } else { // is a file,must be open
                String openFilePath = paths.get(position);
                int indexOfDot = openFilePath.lastIndexOf('.');
                if (indexOfDot >= 0 && indexOfDot <= openFilePath.length()) {
                    if (openFilePath.substring(indexOfDot).equals(new String(".txt"))) {
                        sendFilePathToNotepadActivity(openFilePath);
                    } else {
                        showMessageDialog("只能打开文本文件");
                    }
                } else {
                    showMessageDialog("文件名异常");
                }
            }
        } else { // can open the file
            showMessageDialog("权限不够");
        }
    }

    /**
     * show a message dialog on the activity
     */
    private void showMessageDialog(String message) {
        new AlertDialog.Builder(this).setTitle("message").setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    /**
     * put the file path to the intent structure, and take the intent to the
     * Notepad activity
     */
    private void sendFilePathToNotepadActivity(String filePath) {
        Intent intent = new Intent();
        intent.setClass(FileBrowser.this, Notepad.class);
        Bundle bundle = new Bundle();
        bundle.putString("filePath", filePath);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        FileBrowser.this.finish();
    }
}
