package org.telegram.ui;

import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.telegram.messenger.AndroidUtilities;

public class ChatsWidgetConfigActivity extends ExternalActionActivity {

    private int creatingAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    @Override
    protected boolean handleIntent(Intent intent, boolean isNew, boolean restore, boolean fromPassword, int intentAccount, int state) {
        Log.e("Info log","ChatWidgetConfigActivity.java" );
        if (!checkPasscode(intent, isNew, restore, fromPassword, intentAccount, state)) {
            return false;
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            creatingAppWidgetId = extras.getInt(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }
        if (creatingAppWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID) {
            Bundle args = new Bundle();
            args.putBoolean("onlySelect", true);
            args.putInt("dialogsType", 10);
            args.putBoolean("allowSwitchAccount", true);
            EditWidgetActivity fragment = new EditWidgetActivity(EditWidgetActivity.TYPE_CHATS, creatingAppWidgetId, false);
            fragment.setDelegate(dialogs -> {
                Intent resultValue = new Intent();
                resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, creatingAppWidgetId);
                setResult(RESULT_OK, resultValue);
                finish();
            });

            if (AndroidUtilities.isTablet()) {
                if (layersActionBarLayout.fragmentsStack.isEmpty()) {
                    layersActionBarLayout.addFragmentToStack(fragment);
                }
            } else {
                if (actionBarLayout.fragmentsStack.isEmpty()) {
                    actionBarLayout.addFragmentToStack(fragment);
                }
            }
            if (!AndroidUtilities.isTablet()) {
                backgroundTablet.setVisibility(View.GONE);
            }
            actionBarLayout.showLastFragment();
            if (AndroidUtilities.isTablet()) {
                layersActionBarLayout.showLastFragment();
            }
            intent.setAction(null);
        } else {
            finish();
        }
        return true;
    }
}
