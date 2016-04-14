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

package de.micromata.genome.util.runtime.config;

import java.io.File;

import org.junit.Test;

import de.micromata.genome.util.runtime.LocalSettings;

/**
 * 
 * @author Roger Rene Kommer (r.kommer.extern@micromata.de)
 *
 */
public class LocalSettingsReadMergeWriteTest
{
  @Test
  public void testIt()
  {
    ExtLocalSettingsLoader loader = new ExtLocalSettingsLoader();
    loader.setLocalSettingsFileName("./dev/extrc/test/properties/ls_with_comments1.properties");
    LocalSettings localSettings = loader.loadSettings();

    MergingLocalSettingsWriter lswout = new MergingLocalSettingsWriter(loader.origProps);
    LocalSettingsWriter sec = lswout.newSection("Coded");
    sec.put("a", "c");
    sec.put("c", "3", "New Comment");
    lswout.store(new File("./target/merged.properties"));
    // check manual written file.

  }
}
