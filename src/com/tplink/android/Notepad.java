/*
 * Copyright (C) 2013, TP-LINK TECHNOLOGIES CO., LTD.
 *
 * File name: Notepad.java
 * 
 * Description: the notepad activity, include the area of edit,imaging buttons,menu
 *  
 * Author: fuping
 * 
 * Ver 1.0, 2013-5-3, fuping, Create file
 */

package com.tplink.android;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * The class Notepad
 */
public class Notepad extends Activity {

    private String workDirectory = "/mnt/sdcard/";

    private String tmpFilePath = "temp_file_path";

    private String keyFilePath = "key_file_path";

    private String saveFileName = null;

    private EditText editText;

    private EditText newFilePath;

    private EditText filePathView;

    /**
     * saveFileFlag=true:indicated that the file is been saved; =false:indicated
     * that the file has not been saved
     */
    private boolean saveFileFlag = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);

        filePathView = (EditText)findViewById(R.id.file_path);
        ImageButton btNew = (ImageButton)findViewById(R.id.button_new);
        ImageButton btOpen = (ImageButton)findViewById(R.id.button_open);
        ImageButton btSave = (ImageButton)findViewById(R.id.button_save);
        ImageButton btSaveAs = (ImageButton)findViewById(R.id.button_save_as);
        editText = (EditText)findViewById(R.id.EditTextOpenFile);
        newFilePath = (EditText)findViewById(R.id.new_filePath);
        Button btOk = (Button)findViewById(R.id.ok);
        Button btCancel = (Button)findViewById(R.id.cancel);

        /*
         * corresponding to the button of new, clear the area of the EditText
         */
        btNew.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                editText.setText("");
                saveFileName = null;
                saveFileFlag = false;
            }
        });

        /*
         * corresponding to the button of open, jump to the FileBrowser activity
         */
        btOpen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveFileFlag = false;
                Intent intent = new Intent();
                intent.setClass(Notepad.this, FileBrowser.class);
                startActivityForResult(intent, 0); // indicate open the file

            }
        });

        /*
         * corresponding to the button of save, check the state of external
         * storage, if the SDCard is available, save the file to the SDCard
         */
        btSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    if (saveFileName == null)
                        showMessageDialog("请输入文件名");
                    else {
                        String fileName = workDirectory + saveFileName + ".txt";
                        saveEditTextToFile(fileName);
                        saveFileFlag = true;
                        filePathView.setText(fileName);
                        showMessageDialog("文件保存成功");
                    }
                } else {
                    showMessageDialog("SD卡未连接");
                }
            }
        });

        /*
         * corresponding to the button of ok, press the button we read the
         * directory from the EditText and save file
         */
        btOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                saveFileName = newFilePath.getText().toString();
                if (saveFileName == "")
                    showMessageDialog("输入文件名有误");
                else {
                    String fileName = workDirectory + saveFileName + ".txt";
                    saveEditTextToFile(fileName);
                    saveFileFlag = true;
                    filePathView.setText(fileName);
                    showMessageDialog("文件保存成功");
                }
            }
        });

        /*
         * corresponding to the button of cancel, the notepad is finish
         */
        btCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (!saveFileFlag)
                    showMessageDialog("请先保存文件");
                else {
                    Notepad.this.finish();
                }
            }
        });
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
     * save the content of EditText to the corresponding filePath
     */
    private void saveEditTextToFile(String filePathToSave) {
        try {
            FileOutputStream fileSave = new FileOutputStream(filePathToSave);
            OutputStreamWriter fileWriter = new OutputStreamWriter(fileSave);
            BufferedWriter fileOut = new BufferedWriter(fileWriter);

            TextView openFileView = (TextView)findViewById(R.id.EditTextOpenFile);
            fileOut.write(openFileView.getText().toString());
        } catch (Exception e) {
            showMessageDialog("文件保存失败");
        }

    }

    /**
     * the process return from the FileBrowser activity, get the directory of
     * the file which is being open, open the text file and put the content on
     * the EditText
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /* return the directory of file which is being opened */
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                Bundle bundle = data.getExtras();
                if (bundle != null) {
                    String openFilePath = bundle.getString("filePath");
                    saveFileName = openFilePath;
                    try {
                        FileInputStream fileOpen = new FileInputStream(openFilePath);
                        InputStreamReader fileRead = new InputStreamReader(fileOpen);
                        BufferedReader fileIn = new BufferedReader(fileRead);
                        /* put the content of text on the TextView */
                        TextView openFileView = (TextView)findViewById(R.id.EditTextOpenFile);
                        String strLine;
                        String strFile = new String();
                        while ((strLine = fileIn.readLine()) != null)
                            strFile = strFile.concat(strLine);
                        openFileView.setText(strFile);
                    } catch (Exception e) {
                        showMessageDialog("文件打开失败");
                    }
                }
            }
        }
    }

    /**
     * the Menu item
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_notepad, menu);
        return true;
    }

}
