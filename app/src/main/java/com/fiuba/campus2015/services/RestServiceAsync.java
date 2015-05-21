package com.fiuba.campus2015.services;

import android.os.AsyncTask;
import retrofit.RetrofitError;

public class RestServiceAsync<ReturnType, RestService> {

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

                    ReturnType res = (ReturnType) getResult.getResult(service);
                    if(res!=null) {
                    }
                    return res;
                } catch (RetrofitError retroError) {
                    errorResponse.status = retroError.getResponse().getStatus();
                    errorResponse.reason = retroError.getResponse().getReason();
                    return null;
                } catch(Exception e) {
                    return null;
                }
            }
            @Override
            protected void onPostExecute(ReturnType res) {
                if(res!=null) {
                    Application.getEventBus().post(res);
                } else /*if(errorResponse!=null)*/ {
                    Application.getEventBus().post(errorResponse);
                }
            }
        }.execute();
    }

}