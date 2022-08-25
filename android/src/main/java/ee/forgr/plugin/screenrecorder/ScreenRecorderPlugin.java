package ee.forgr.plugin.screenrecorder;

import android.util.DisplayMetrics;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;

import java.io.File;

import dev.bmcreations.scrcast.ScrCast;
import dev.bmcreations.scrcast.config.Options;
import dev.bmcreations.scrcast.config.VideoConfig;
import dev.bmcreations.scrcast.recorder.RecordingCallbacks;
import dev.bmcreations.scrcast.recorder.RecordingState;

@CapacitorPlugin(name = "ScreenRecorder")
public class ScreenRecorderPlugin extends Plugin {

    private ScrCast recorder;

    @Override
    public void load() {
        recorder =  ScrCast.use(this.bridge.getActivity());
        Options options = new Options();
        recorder.updateOptions(options);
    }

    @PluginMethod
    public void start(PluginCall call) {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        this.bridge.getActivity().getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);
        VideoConfig videoConfig = new VideoConfig(displayMetrics.widthPixels, displayMetrics.heightPixels);
        recorder.updateOptions(new Options(videoConfig));
        recorder.record();
        call.resolve();
    }
    
    @PluginMethod
    public void stop(PluginCall call) {
        recorder.setRecordingCallback(new RecordingCallbacks() {
            @Override
            public void onStateChange(@NonNull RecordingState recordingState) {
            }

            @Override
            public void onRecordingFinished(@NonNull File file) {
                JSObject ret = new JSObject();
                ret.put("url", "file://" + file.getAbsolutePath());
                call.resolve(ret);
            }
        });
        recorder.stopRecording();
    }
}
