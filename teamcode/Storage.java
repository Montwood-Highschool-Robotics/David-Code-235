package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.SwitchableLight;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvWebcam;

@TeleOp
public class
Storage extends LinearOpMode {
    private DcMotor FLW;
    private DcMotor FRW;
    private DcMotor BLW;
    private DcMotor BRW;
    private DcMotor A;
    private DcMotor B;
    private DcMotor C;
    private DcMotor D;
    //private Servo Servo;
    //private CRServo CRServo;
    private CRServo C1;
    private CRServo C2;
    private CRServo C3;
    private ElapsedTime runtime = null;
    NormalizedColorSensor colorSensor;
    View relativeLayout;
    static final int STREAM_WITH = 1920;
    static final double STREAM_HEIGHT = 1080;
    OpenCvWebcam webcam;
    SamplePipeline pipeline;


    @Override
    public void runOpMode() throws InterruptedException {


        DcMotor FRW = hardwareMap.dcMotor.get("FRW");
        DcMotor FLW = hardwareMap.dcMotor.get("FLW");
        DcMotor BRW = hardwareMap.dcMotor.get("BRW");
        DcMotor BLW = hardwareMap.dcMotor.get("BLW");
        DcMotor A = hardwareMap.dcMotor.get("A");
        DcMotor B = hardwareMap.dcMotor.get("B");
        DcMotor C = hardwareMap.dcMotor.get("C");
        DcMotor D = hardwareMap.dcMotor.get("D");
        //Servo Servo = hardwareMap.servo.get("Servo");
        //CRServo CRServo = hardwareMap.crservo.get("CRServo");
        CRServo C1 = hardwareMap.crservo.get("C1");
        CRServo C2 = hardwareMap.crservo.get("C2");
        CRServo C3 = hardwareMap.crservo.get("C3");

        int relativeLayoutID = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "ID", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutID);

/*
        try {
            runSample();
        } finally {
            relativeLayout.post(new Runnable() {
                @Override
                public void run() {
                    relativeLayout.setBackgroundColor(Color.MAGENTA);
                }
            });
        }

 */


        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {


            FLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            A.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            B.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            C.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            D.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            //Servo.setPosition(0);

            BRW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FRW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BLW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FLW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            A.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            B.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            C.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            D.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FRW.setDirection(DcMotorSimple.Direction.REVERSE);
            BRW.setDirection(DcMotorSimple.Direction.REVERSE);
            //CRServo.setDirection(DcMotorSimple.Direction.FORWARD);
            C1.setDirection(DcMotorSimple.Direction.FORWARD);
            C2.setDirection(DcMotorSimple.Direction.FORWARD);
            C3.setDirection(DcMotorSimple.Direction.FORWARD);

            telemetry.addData("Active:", "Set Up Complete");
            /*
            telemetry.addData("Numbers:", "1");
            */
            telemetry.update();

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = .8 * (gamepad1.right_stick_x);
            double a = gamepad2.left_stick_y;
            double b = gamepad2.left_stick_x;
            double c = .5 * (gamepad2.right_stick_x);
            /*
            int currentPosition1 = FLW.getCurrentPosition();
            */
            double z = gamepad2.left_stick_y;
            double z1 = gamepad2.right_stick_y;
            double z2 = 1;


            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), .65);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            double ABCDPower = 1;
            double crservoPower = (z1 / 3);
            double crservoPowerPlus = (z2 * 1);
            /*
            double a = -gamepad1.left_stick_y;
            double b = gamepad1.left_stick_x;
            double c = (Math.abs(a) + Math.abs(b) * 2);
             */
            FLW.setPower(frontLeftPower * 1);
            BLW.setPower(backLeftPower * 1);
            FRW.setPower(frontRightPower * -1);
            BRW.setPower(backRightPower * -1);
            //CRServo.setPower(crservoPower * 1);

            //Hammer (of Justice)
            if (gamepad1.right_trigger < 0.5) {
                C3.setPower(-crservoPowerPlus / 3);
            } else if (gamepad1.left_trigger < 0.5) {
                C3.setPower(crservoPowerPlus / 3);
            } else {
                C3.setPower(crservoPowerPlus - 1);
            }

            /* Rotator
            if (gamepad2.dpad_left) {
                C1.setPower(-crservoPowerPlus / 2);
            } else if (gamepad2.dpad_right) {
                C1.setPower(crservoPowerPlus / 2);
            } else {
                C1.setPower(0);
            }
            */

            //Shoot Chamber Angle
            if (gamepad2.dpad_down) {
                C2.setPower(-crservoPowerPlus / 3);
            } else if (gamepad2.dpad_up) {
                C2.setPower(crservoPowerPlus / 3);
            } else {
                C2.setPower(0);
            }

            //Shooter
            if (gamepad2.triangle) {
                A.setPower(ABCDPower);
            } else if (gamepad2.x) {
                A.setPower(-ABCDPower);
            } else {
                A.setPower(0);
            }

            //Intake
            if (gamepad2.circle) {
                B.setPower(ABCDPower);
            } else if (gamepad2.square) {
                B.setPower(-ABCDPower);
            } else {
                B.setPower(0);
            }

            /*Future Slides
            if (gamepad2.right_trigger < 0.5) {
                C.setPower(ABCDPower);
                D.setPower(ABCDPower);
            } else if (gamepad2.left_trigger < 0.5) {
                C.setPower(-ABCDPower);
                D.setPower(-ABCDPower);
            } else {
                C.setPower(0);
                D.setPower(0);
            }
            */

            /*
            FRW.setPower(a / b);
            FLW.setPower(b - a);
            BRW.setPower(c + b);
            BLW.setPower(c * a);
            */

            /*
            if (gamepad1.a) {
                telemetry.addData("FLW Value:", currentPosition1);
                telemetry.update();
            } else {
                telemetry.clear();
                telemetry.update();
            } if (gamepad1.b) {
                telemetry.clear();
                telemetry.update();
            } if (gamepad1.cross) {
                runtime.reset();
                telemetry.addData("Time in Seconds:", time);
                telemetry.addData("Time Since Recording Start:", runtime.toString());
            }
            */

        }
    }


/*
    protected void runSample() {
        float gain = 2;
        final float[] hsvValues = new float[3];
        boolean xButtonPreviouslyPressed = false;
        boolean xButtonCurrentlyPressed = false;
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "ColorSensor");

        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight) colorSensor).enableLight(true);
        }
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n");
            telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

            double z3 = 0.5;
            double crservoPower = (z3 * 2);

            if (gamepad1.a) {
                gain += 0.005;
            } else if (gamepad1.b && gain > 1) {
                gain -= 0.005;
            }
            telemetry.addData("Gain", gain);
            colorSensor.setGain(gain);
            xButtonCurrentlyPressed = gamepad1.x;

            if (xButtonCurrentlyPressed != xButtonPreviouslyPressed) {
                if (xButtonCurrentlyPressed) {
                    if (colorSensor instanceof SwitchableLight) {
                        SwitchableLight light = (SwitchableLight) colorSensor;
                        light.enableLight(!light.isLightOn());
                    }
                }
            }
            xButtonPreviouslyPressed = xButtonCurrentlyPressed;
            NormalizedRGBA colors = colorSensor.getNormalizedColors();
            Color.colorToHSV(colors.toColor(), hsvValues);

            telemetry.addLine()
                    .addData("Red", "%.3f", colors.red)
                    .addData("Green", "%.3f", colors.green)
                    .addData("Blue", "%.3f", colors.blue);
            telemetry.addLine()
                    .addData("Hue", "%.3f", hsvValues[0])
                    .addData("Saturation", "%.3f", hsvValues[1])
                    .addData("Value", "%.3f", hsvValues[2])
                    .addData("Alpha", "%.3f", colors.alpha);
            telemetry.update();

            relativeLayout.post(new Runnable() {
                @Override
                public void run() {
                    relativeLayout.setBackgroundColor(Color.HSVToColor(hsvValues));
                }
            });
            if (colors.red == 100 && colors.green == 212 && colors.blue == 170) {
                C1.setPower(0);
            } else {
                C1.setPower(-crservoPower / 3);
            }
        }
    }
 */
}
/*
abstract class
NotStorage extends OpMode {
    static final int STREAM_WITH = 1920;
    static final double STREAM_HEIGHT = 1080;
    OpenCvWebcam webcam;
    SamplePipeline pipeline;
    @Override
    public void init() {
        int cameraMonitorViewID =
                hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = null;
        webcamName = hardwareMap.get(WebcamName.class, "WebcamMain");
        webcam = OpenCvCameraFactory.getInstance().createWebcam(webcamName, cameraMonitorViewID);
        pipeline = new SamplePipeline();
        webcam.setPipeline(pipeline);
        webcam.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onError(int i) {

            }

            @Override
            public void onOpened() {
                webcam.startStreaming(STREAM_WITH, (int) STREAM_HEIGHT, OpenCvCameraRotation.UPRIGHT);
            }
            public void OnError(int errorCode) {
                telemetry.addData("Camera Failed", "");
                telemetry.update();
            }
        });
    }
    @Override
    public void loop() {
        telemetry.addData("Image Analysis", pipeline.getAnalysis());
    }
}
*/