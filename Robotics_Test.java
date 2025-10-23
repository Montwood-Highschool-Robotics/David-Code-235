package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.SwitchableLight;

@TeleOp
public class
Robotics_Test extends LinearOpMode {
    private DcMotor FLW;
    private DcMotor FRW;
    private DcMotor BLW;
    private DcMotor BRW;
    private Servo Servo;
    private CRServo CRServo;
    private ElapsedTime runtime = null;
    NormalizedColorSensor colorSensor;
    View relativeLayout;

    @Override
    public void runOpMode() throws InterruptedException {


        DcMotor FRW = hardwareMap.dcMotor.get("FRW");
        DcMotor FLW = hardwareMap.dcMotor.get("FLW");
        DcMotor BRW = hardwareMap.dcMotor.get("BRW");
        DcMotor BLW = hardwareMap.dcMotor.get("BLW");
        Servo Servo = hardwareMap.servo.get("Servo");
        CRServo CRServo = hardwareMap.crservo.get("CRServo");

        int relativeLayoutID = hardwareMap.appContext.getResources().getIdentifier("RelativeLayout", "ID", hardwareMap.appContext.getPackageName());
        relativeLayout = ((Activity) hardwareMap.appContext).findViewById(relativeLayoutID);

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


        waitForStart();
        runtime.reset();
        while (opModeIsActive()) {


            FLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BLW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            FRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            BRW.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Servo.setPosition(1);

            BRW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FRW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            BLW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FLW.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            FRW.setDirection(DcMotorSimple.Direction.REVERSE);
            BRW.setDirection(DcMotorSimple.Direction.REVERSE);
            CRServo.setDirection(DcMotorSimple.Direction.FORWARD);

            telemetry.addData("Active:","Set Up Complete");
            /*
            telemetry.addData("Numbers =", "1");
            */
            telemetry.update();

            double y = -gamepad1.left_stick_y;
            double x = gamepad1.left_stick_x;
            double rx = .8 * (gamepad1.right_stick_x);
            /*
            int currentPosition1 = FLW.getCurrentPosition();
            */
            double z = gamepad2.left_stick_y;
            double z1 = gamepad2.right_stick_y;


            double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), .65);
            double frontLeftPower = (y + x + rx) / denominator;
            double backLeftPower = (y - x + rx) / denominator;
            double frontRightPower = (y - x - rx) / denominator;
            double backRightPower = (y + x - rx) / denominator;
            double servoPosition = (z / 10);
            double crservoPower = (z1 / 10);
            /*
            double a = -gamepad1.left_stick_y;
            double b = gamepad1.left_stick_x;
            double c = (Math.abs(a) + Math.abs(b) * 2);
             */
            FLW.setPower(frontLeftPower * 1);
            BLW.setPower(backLeftPower * 1);
            FRW.setPower(frontRightPower * -1);
            BRW.setPower(backRightPower * -1);
            Servo.setPosition(servoPosition);
            CRServo.setPower(crservoPower * 1);
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

    protected void runSample() {
        float gain = 2;
        final float[] hsvValues = new float[3];
        boolean xButtonPreviouslyPressed = false;
        boolean xButtonCurrentlyPressed = false;
        colorSensor = hardwareMap.get(NormalizedColorSensor.class, "ColorSensor");

        if (colorSensor instanceof SwitchableLight) {
            ((SwitchableLight)colorSensor).enableLight(true);
        }
        waitForStart();
        while (opModeIsActive()) {
            telemetry.addLine("Hold the A button on gamepad 1 to increase gain, or B to decrease it.\n");
            telemetry.addLine("Higher gain values mean that the sensor will report larger numbers for Red, Green, and Blue, and Value\n");

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
                        SwitchableLight light = (SwitchableLight)colorSensor;
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
                Servo.setPosition(0.3);
            } else {
                Servo.setPosition(0);
            }
        }
    }
}