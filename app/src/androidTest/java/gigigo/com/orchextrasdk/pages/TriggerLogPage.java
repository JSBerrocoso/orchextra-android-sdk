/*
 * Created by Orchextra
 *
 * Copyright (C) 2017 Gigigo Mobile Services SL
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package gigigo.com.orchextrasdk.pages;

import android.content.Context;
import android.support.test.espresso.ViewInteraction;
import gigigo.com.orchextrasdk.R;
import gigigo.com.orchextrasdk.utils.TestUtils;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;

public class TriggerLogPage extends DemoPage {

  public TriggerLogPage(Context context) {
    super(context);
  }

  public TriggerLogPage checkIfLogListIsEmpty() {

    ViewInteraction linearLayout = onView(allOf(withId(R.id.empty_list_view),
        TestUtils.childAtPosition(TestUtils.childAtPosition(withId(R.id.fragment_container), 0), 0),
        isDisplayed()));
    linearLayout.check(matches(isDisplayed()));

    return this;
  }
}