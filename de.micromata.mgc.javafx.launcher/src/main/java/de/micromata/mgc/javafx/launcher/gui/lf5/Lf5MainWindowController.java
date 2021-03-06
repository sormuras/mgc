//
// Copyright (C) 2010-2016 Micromata GmbH
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//  http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
//

package de.micromata.mgc.javafx.launcher.gui.lf5;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;

import org.apache.log4j.lf5.LogLevel;

import de.micromata.mgc.application.MgcApplication;
import de.micromata.mgc.javafx.launcher.gui.AbstractModelController;
import javafx.embed.swing.SwingNode;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;

public class Lf5MainWindowController extends AbstractModelController<MgcApplication<?>>
{
  public static Lf5MainWindowController CONTROLERINSTANCE;
  static MgcLogBrokerMonitor MONITORINSTANCE;

  public static MgcLogBrokerMonitor getMonitor()
  {
    return MONITORINSTANCE;
  }

  public static Lf5MainWindowController getController()
  {
    return CONTROLERINSTANCE;
  }

  @FXML
  private Pane contentPane;

  private SwingNode swingNode;
  JRootPane rootLF5Pane;

  @Override
  public void initializeWithModel()
  {
    swingNode = new SwingNode()
    {
      @Override
      public boolean isResizable()
      {
        return true;
      }

      @Override
      public void resize(double width, double height)
      {
        //        System.out.println("SwingNode.resize(" + width + "," + height + ")");
        super.resize(width, height);
      }

    };

    createSwingContent(swingNode);
    stage.setTitle("L4J Viewer");
    contentPane.setMinHeight(getStage().getHeight() - 50);
    contentPane.setMinHeight(getStage().getWidth() - 50);
    //    contentPane.setPrefWidth(Double.MAX_VALUE);
    //    contentPane.setPrefHeight(Double.MAX_VALUE);
    contentPane.getChildren().add(swingNode);
    //    closeButton.setOnAction(event -> hide());
    CONTROLERINSTANCE = this;
    scene.widthProperty().addListener((owner, oldValue, newValue) -> {
      rootLF5Pane.setMinimumSize(new Dimension((int) getStage().getWidth(), (int) getStage().getHeight()));

      //      System.out.println("Stage With changed: " + newValue);
      //      swingNode.minWidth(newValue.doubleValue());
      //      //      swingNode.prefHeight(width)
      //      swingNode.autosize();
      if (MONITORINSTANCE != null) {
        //        MONITORINSTANCE.setWidth(newValue.doubleValue());
      }
    });

    contentPane.prefWidthProperty().bind(scene.widthProperty());
    contentPane.prefHeightProperty().bind(scene.heightProperty());

    contentPane.widthProperty().addListener((owner, oldValue, newValue) -> {
      swingNode.autosize();
      System.out.println("With changed: " + newValue);
      contentPane.setMinWidth(newValue.doubleValue());
      //      if (MONITORINSTANCE != null) {
      //        MONITORINSTANCE.setWidth(newValue.doubleValue());
      //      }
    });
  }

  public void hide()
  {
    getStage().hide();
    //    MONITORINSTANCE = null;
  }

  public void show()
  {
    getStage().show();
  }

  private void createSwingContent(final SwingNode swingNode)
  {
    SwingUtilities.invokeLater(new Runnable()
    {
      @Override
      public void run()
      {
        MgcLogBrokerMonitor monitor = new MgcLogBrokerMonitor(swingNode, LogLevel.getLog4JLevels());
        JFrame frame = monitor.getMainFrame();
        rootLF5Pane = frame.getRootPane();
        //        rootPane.setBounds(0, 0, (int) getStage().getWidth(), (int) getStage().getHeight());
        rootLF5Pane.setMinimumSize(new Dimension((int) getStage().getWidth(), (int) getStage().getHeight()));
        swingNode.setContent(rootLF5Pane);
        MONITORINSTANCE = monitor;
      }
    });
  }
}
