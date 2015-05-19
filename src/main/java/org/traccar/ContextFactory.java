/*
 * Copyright 2015 Anton Tananaev (anton.tananaev@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.traccar;

import java.util.Objects;
import java.util.Properties;

import org.traccar.context.Context;
import org.traccar.database.DataManager;

public abstract class ContextFactory {

	//TODO: Temp solution for avoiding static
	private static Context context;
	
	public static Context getContext() {
		Objects.requireNonNull(context, "Context should be initialized");

		return context;
	}
	
	public static void setContext(Context context) {
		ContextFactory.context = context;
	}

	/**
	 * Initialize context for unit testing
	 */
	public static void init(DataManager dataManager) {
		getContext().setProperties(new Properties());
		getContext().setDataManager(dataManager);
	}

}
