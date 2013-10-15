/*******************************************************************************
 * Copyright (c) 2013 Gabriele Mariotti.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package it.gmariotti.changelibs.library.internal;

/**
 * A simple Exception
 * 
 * @author Gabriele Mariotti (gabri.mariotti@gmail.com)
 */
public class ChangeLogException extends Exception {
    
    /**
     * 
     */
    private static final long serialVersionUID = -2917161371978069994L;
    
    public ChangeLogException(String s) {
	super(s);
    }
}
