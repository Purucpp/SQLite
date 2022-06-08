package com.yesandroid.sqlite;

import javax.inject.Singleton;
import dagger.Component;

// All the modules are mentioned under
// the @Component annotation
@Singleton
@Component(modules = {SharedPreferenceModule.class})
public interface SharedPreferenceComponent {
    void inject(MainActivity mainActivity);
}
