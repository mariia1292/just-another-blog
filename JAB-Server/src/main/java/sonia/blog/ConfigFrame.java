/**
 * Copyright (c) 2009, Sebastian Sdorra
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 3. Neither the name of JAB; nor the names of its
 *    contributors may be used to endorse or promote products derived from this
 *    software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE REGENTS OR CONTRIBUTORS BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 * 
 * http://kenai.com/projects/jab
 * 
 */

/*
 * ConfigFrame.java
 *
 * Created on Mar 7, 2010, 3:09:13 PM
 */
package sonia.blog;

import java.awt.Desktop;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.net.URI;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import sonia.blog.server.BlogServer;
import sonia.blog.server.BlogServerConfig;
import sonia.blog.server.BlogServerException;
import sonia.blog.server.BlogServerFactory;
import sonia.blog.server.BlogServerListener;

/**
 *
 * @author Sebastian Sdorra
 */
public class ConfigFrame extends javax.swing.JFrame implements BlogServerListener
{

  private static Logger logger = Logger.getLogger(ConfigFrame.class.getName());
  private TrayIcon trayIcon = null;
  private BlogServer server;
  private BlogServerConfig config;
  private JFileChooser chooser;

  /** Creates new form ConfigFrame */
  public ConfigFrame(String resourcePath)
  {
    initComponents();
    ed_resourcedir.setText(resourcePath);
    
    if (trayIcon == null && SystemTray.isSupported())
    {
      createTrayIcon();
    }
  }

  private final void createTrayIcon()
  {
    SystemTray systemTray = SystemTray.getSystemTray();
    PopupMenu menu = new PopupMenu();
    MenuItem optionsItem = new MenuItem( "Options" );
    optionsItem.addActionListener( new ActionListener() {

      @Override
      public void actionPerformed(ActionEvent ae)
      {
        setAlwaysOnTop(true);
        setVisible(true);
        setAlwaysOnTop(false);
      }
    });
    
    menu.add(optionsItem);

    MenuItem exitItem = new MenuItem("Exit");
    
    exitItem.addActionListener(new ActionListener()
    {
      
      @Override
      public void actionPerformed(ActionEvent e)
      {
        close();
      }
    });

    menu.add(exitItem);

    try
    {
      Image image = ImageIO.read(getClass().getResource("/icon-64.gif"));

      trayIcon = new TrayIcon(image);
      trayIcon.setPopupMenu(menu);
      trayIcon.setImageAutoSize(true);

      systemTray.add(trayIcon);
    }
    catch (Exception ex)
    {
      handleException(ex);
    }
  }

  private void handleException( Throwable exception )
  {
    logger.log(Level.SEVERE, null, exception);
    JOptionPane.showMessageDialog(getContentPane(), exception.getMessage(),
      "Error", JOptionPane.ERROR_MESSAGE);
    finish();
  }

  public void close()
  {
    if ( server != null )
    {
      try
      {
        server.stop();
      }
      catch (BlogServerException ex)
      {
        logger.log(Level.SEVERE, null, ex);
      }
    }
    
    saveWindow();
    System.exit(0);
  }

  private void saveWindow()
  {
    StringBuffer path = new StringBuffer( System.getProperty( "user.home" ) );
    path.append( File.separator ).append(".jab-server");
  }

  private void load()
  {
    pb_status.setIndeterminate(true);
    ed_contextpath.setEnabled(false);
    ed_port.setEnabled(false);
    ed_resourcedir.setEnabled(false);
    bt_browse.setEnabled(false);
    bt_start.setEnabled(false);
    setVisible(true);
    bt_stop.setEnabled(true);
  }

  private void finish()
  {
    pb_status.setIndeterminate(false);
    ed_contextpath.setEnabled(true);
    ed_port.setEnabled(true);
    ed_resourcedir.setEnabled(true);
    bt_browse.setEnabled(true);
    bt_start.setEnabled(true);
    bt_stop.setEnabled(false);
  }

  @Override
  public void failed(Throwable throwable)
  {
    handleException(throwable);
  }

  @Override
  public void started()
  {
    pb_status.setIndeterminate(false);
    setVisible(false);
    if (Desktop.isDesktopSupported())
    {
      StringBuffer buffer = new StringBuffer("http://localhost:");
      buffer.append(config.getPort()).append(config.getContextPath());
      try
      {
        Desktop.getDesktop().browse(new URI(buffer.toString()));
      }
      catch (Exception ex)
      {
        handleException(ex);
      }
    }
  }

  @Override
  public void stopped()
  {
    finish();
  }

  /** This method is called from within the constructor to
   * initialize the form.
   * WARNING: Do NOT modify this code. The content of this method is
   * always regenerated by the Form Editor.
   */
  @SuppressWarnings("unchecked")
  // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
  private void initComponents() {

    la_port = new javax.swing.JLabel();
    ed_port = new javax.swing.JTextField();
    la_resourcedir = new javax.swing.JLabel();
    ed_resourcedir = new javax.swing.JTextField();
    la_contextpath = new javax.swing.JLabel();
    ed_contextpath = new javax.swing.JTextField();
    bt_browse = new javax.swing.JButton();
    bt_start = new javax.swing.JButton();
    pb_status = new javax.swing.JProgressBar();
    bt_stop = new javax.swing.JButton();

    setTitle("JAB-Server");
    setResizable(false);

    la_port.setText("Port:");

    ed_port.setText("8080");

    la_resourcedir.setText("Resource-Directory:");

    la_contextpath.setText("Context-Path:");

    ed_contextpath.setText("/");

    bt_browse.setText("...");
    bt_browse.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_browseActionPerformed(evt);
      }
    });

    bt_start.setText("Start");
    bt_start.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_startActionPerformed(evt);
      }
    });

    bt_stop.setText("Stop");
    bt_stop.setEnabled(false);
    bt_stop.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        bt_stopActionPerformed(evt);
      }
    });

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
    getContentPane().setLayout(layout);
    layout.setHorizontalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
          .addGroup(layout.createSequentialGroup()
            .addGap(20, 20, 20)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(la_contextpath)
              .addComponent(la_resourcedir)
              .addComponent(la_port))
            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addGroup(layout.createSequentialGroup()
                .addComponent(ed_resourcedir, javax.swing.GroupLayout.PREFERRED_SIZE, 248, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(bt_browse, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
              .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                .addComponent(ed_contextpath, javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(ed_port, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 81, Short.MAX_VALUE))))
          .addGroup(layout.createSequentialGroup()
            .addContainerGap()
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
              .addComponent(pb_status, javax.swing.GroupLayout.DEFAULT_SIZE, 464, Short.MAX_VALUE)
              .addGroup(layout.createSequentialGroup()
                .addComponent(bt_start, javax.swing.GroupLayout.DEFAULT_SIZE, 237, Short.MAX_VALUE)
                .addComponent(bt_stop, javax.swing.GroupLayout.PREFERRED_SIZE, 227, javax.swing.GroupLayout.PREFERRED_SIZE)))))
        .addContainerGap())
    );
    layout.setVerticalGroup(
      layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
      .addGroup(layout.createSequentialGroup()
        .addContainerGap()
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(ed_port, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(la_port))
        .addGap(5, 5, 5)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(ed_contextpath, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(la_contextpath))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(ed_resourcedir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
          .addComponent(la_resourcedir)
          .addComponent(bt_browse))
        .addGap(18, 18, 18)
        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
          .addComponent(bt_stop)
          .addComponent(bt_start))
        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
        .addComponent(pb_status, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
        .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
    );

    pack();
  }// </editor-fold>//GEN-END:initComponents

    private void bt_startActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_startActionPerformed
    {//GEN-HEADEREND:event_bt_startActionPerformed

      load();

      String contextPath = ed_contextpath.getText();
      String resourcePath = ed_resourcedir.getText();
      Integer port = Integer.parseInt(ed_port.getText());

      config = new BlogServerConfig(new File(resourcePath), port, contextPath);
      server = BlogServerFactory.newServer(config);
      server.addServerListener(this);

      try
      {

        new Thread(new Runnable()
        {

          @Override
          public void run()
          {
            try
            {
              server.start();
            }
            catch (Exception ex)
            {
              handleException(ex);
            }
          }
        }).start();

      }
      catch (Exception ex)
      {
        handleException(ex);
      }
    }//GEN-LAST:event_bt_startActionPerformed

    private void bt_stopActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_stopActionPerformed
    {//GEN-HEADEREND:event_bt_stopActionPerformed
      if ( server != null )
      {
        try
        {
          server.stop();
        }
        catch (BlogServerException ex)
        {
          handleException(ex);
        }
      }
    }//GEN-LAST:event_bt_stopActionPerformed

    private void bt_browseActionPerformed(java.awt.event.ActionEvent evt)//GEN-FIRST:event_bt_browseActionPerformed
    {//GEN-HEADEREND:event_bt_browseActionPerformed
      if ( chooser == null )
      {
        chooser = new JFileChooser();
        String path = ed_resourcedir.getText();
        if ( path != null && path.length() > 0 )
        {
          File directory = new File( path );
          if ( directory.exists() && directory.isDirectory() )
          {
            chooser.setSelectedFile(directory);
          }
        }
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY );
      }
      int returnVal = chooser.showOpenDialog(this);
      if ( returnVal == JFileChooser.APPROVE_OPTION ){
        ed_resourcedir.setText(chooser.getSelectedFile().getPath());
      }
    }//GEN-LAST:event_bt_browseActionPerformed

  // Variables declaration - do not modify//GEN-BEGIN:variables
  private javax.swing.JButton bt_browse;
  private javax.swing.JButton bt_start;
  private javax.swing.JButton bt_stop;
  private javax.swing.JTextField ed_contextpath;
  private javax.swing.JTextField ed_port;
  private javax.swing.JTextField ed_resourcedir;
  private javax.swing.JLabel la_contextpath;
  private javax.swing.JLabel la_port;
  private javax.swing.JLabel la_resourcedir;
  private javax.swing.JProgressBar pb_status;
  // End of variables declaration//GEN-END:variables

}
