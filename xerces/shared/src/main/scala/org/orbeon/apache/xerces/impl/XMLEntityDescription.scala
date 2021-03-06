/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.orbeon.apache.xerces.impl

import org.orbeon.apache.xerces.xni.XMLResourceIdentifier

/**
 * This interface describes the properties of entities--their
 * physical location and their name.
 */
trait XMLEntityDescription extends XMLResourceIdentifier {

  /**
   * Sets the name of the entity.
   *
   * @param name the name of the entity
   */
  def setEntityName(name: String): Unit

  /**
   * Returns the name of the entity.
   *
   * @return the name of the entity
   */
  def getEntityName: String
}
