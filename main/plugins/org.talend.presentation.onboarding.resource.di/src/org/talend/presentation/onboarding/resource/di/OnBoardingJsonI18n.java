package org.talend.presentation.onboarding.resource.di;

import org.talend.presentation.onboarding.interfaces.IOnBoardingJsonI18n;
import org.talend.presentation.onboarding.resource.di.i18n.Messages;

public class OnBoardingJsonI18n implements IOnBoardingJsonI18n {

    public OnBoardingJsonI18n() {
        // nothing need to do
    }

    @Override
    public String getI18NString(String key) {
        return Messages.getString(key);
    }

}
