package com.shivtech.notificationchannels.Utils;

import android.os.AsyncTask;



public class BackgroundTaskHelper extends AsyncTask<Void,Void,Void> {
    public interface  BackgroundTaskListener{
        void doTask() ;
        void doAfterTask();
    }
    BackgroundTaskListener backgroundTaskListener;
    public BackgroundTaskHelper(BackgroundTaskListener backgroundTaskListener){
        this.backgroundTaskListener  = backgroundTaskListener;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        backgroundTaskListener.doTask();
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        backgroundTaskListener.doAfterTask();
        backgroundTaskListener = null;
    }
}
