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

package de.micromata.mgc.javafx;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.paint.Color;

/**
 * Utility-Methods for working with JavaFX CSS.
 * 
 * @author Daniel (d.ludwig@micromata.de)
 *
 */
public class FXCssUtil
{
  /**
   * POC CSS Filepath.
   */
  public static final String CSS = "/fxml/styles.css";

  /**
   * Replaces one css class with another. If the css class wasn't found the new class is simply added.
   * 
   * @param oldClass class to replace
   * @param newClass class to replace with
   * @param node the node.
   */
  public static void replaceStyleClass(String oldClass, String newClass, Node node)
  {
    final ObservableList<String> styleClass = node.getStyleClass();
    int indexOf = styleClass.indexOf(oldClass);
    if (indexOf == -1) {
      indexOf = styleClass.indexOf(newClass);
      if (indexOf == -1) {
        styleClass.add(newClass);
      }
    } else {
      styleClass.remove(indexOf);
      styleClass.add(indexOf, newClass);
    }
  }

  /**
   * Converts a Color Object to a rgb(..%, ..%, ..%) statement that can be used in a css style definition.
   * 
   * @param color Color to convert.
   * @return see description.
   */
  public static String cssRGBStatement(Color color)
  {
    return "rgb(" + (int) (color.getRed() * 100) + "%, " + (int) (color.getGreen() * 100) + "%, "
        + (int) (color.getBlue() * 100) + "%)";
  }
}
