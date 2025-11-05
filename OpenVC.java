package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;
import org.openftc.easyopencv.OpenCvTrackerApiPipeline;
import org.openftc.easyopencv.OpenCvWebcam;
import org.openftc.easyopencv.PipelineRecordingParameters;

import java.util.ArrayList;

@TeleOp
public abstract class
OpenVC extends OpMode {
    static final int STREAM_WITH = 1920;
    static final double STREAM_HEIGHT = 1080;
    OpenCvWebcam webcam;
}

class SamplePipeline extends OpenCvPipeline {
    Mat YCrCb = new Mat();
    Mat Y = new Mat();
    int avg;

    void inputToY(Mat input) {
        Imgproc.cvtColor(input, YCrCb, Imgproc.COLOR_RGB2YCrCb);
        ArrayList<Mat> yCrCbChannels = new ArrayList<Mat>(3);
        Core.split(YCrCb, yCrCbChannels);
        Y = yCrCbChannels.get(0);
    }
    @Override
    public void init (Mat firstFrame) {
        inputToY(firstFrame);
    }
    @Override
    public Mat processFrame(Mat input) {
        inputToY(input);
        System.out.println("Processing Requested");
        avg = (int) Core.mean(Y).val[0];
        YCrCb.release();
        return input;
    }
    public int getAnalysis() {
        return avg;
    }
}
