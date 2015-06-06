package com.fiuba.campus2015.services;

import android.os.AsyncTask;
import retrofit.RetrofitError;

public class PoolingService<ReturnType, RestService> {

    public static abstract class GetResult<ReturnResult, ServiceClass>  {
        public abstract ReturnResult getResult(ServiceClass mService);
    }

    public void fetch(
            final RestService service,
            final GetResult getResult,
            final Response errorResponse
    ) {
        new AsyncTask<Void, Void, ReturnType>() {

            @Override
            protected ReturnType doInBackground(Void... params) {
                try {

                    getResult.getResult(service);
                } catch (RetrofitError retroError) {
                    errorResponse.status = retroError.getResponse().getStatus();
                    errorResponse.reason = retroError.getResponse().getReason();
                } catch(Exception e) {
                }
                return null;

            }
            @Override
            protected void onPostExecute(ReturnType res) {
            }
        }.execute();
    }

}