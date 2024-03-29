package org.tranphucbol.predictivetext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class DictionaryUI {
    private JFrame frame;
    private JPanel panelMain;
    private JTextField wordTxt;
    private JList wordList;
    private Trie trie;
    private boolean done;


    private JPanel panelDiaLog;
    private JLabel loadingLabel;

    private static final Logger logger = LoggerFactory.getLogger(DictionaryUI.class);

    public DictionaryUI() {
        frame = new JFrame("Dictionary");
        frame.setContentPane(panelMain);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        done = false;

        panelDiaLog = new JPanel();

        panelDiaLog.setPreferredSize(new Dimension(200, 50));
        loadingLabel = new JLabel("Loading ...");
        panelDiaLog.add(loadingLabel);

        final JDialog dialog = new JDialog(frame, "Loading", true);
        dialog.getContentPane().add(panelDiaLog);
        dialog.setUndecorated(true);
        dialog.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        new Thread(new Runnable() {
            public void run() {
                processBar("Loading ");
                try {
                    trie = Trie.read();
                } catch (IOException e) {
                    logger.error("File Trie doesn't exist");
                    trie = new Trie("/home/cpu11413/Documents/blogs", new BlogFile());
                    trie.writeFile();
                }
                done = true;
                dialog.dispose();
            }
        }).start();

        dialog.pack();
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);


        wordTxt.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                super.keyReleased(e);
                wordList.setListData(trie.startWith(wordTxt.getText()).toArray());
            }
        });
    }

    private void processBar(final String message) {
        done = false;
        new Thread(new Runnable() {
            public void run() {
                int count = 0;
                int maxCount = 3;
                while(!done) {
                    try {
                        Thread.sleep(500);
                        StringBuffer buffer = new StringBuffer(message);

                        if(count == maxCount)
                            count = 0;
                        else count++;

                        for(int i=0; i<count; i++) {
                            buffer.append('.');
                        }

                        loadingLabel.setText(buffer.toString());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                loadingLabel.setText("Done");
            }
        }).start();
    }
}
