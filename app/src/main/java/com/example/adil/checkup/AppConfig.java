package com.example.adil.checkup;

/**
 * Created by adil on 4/20/17.
 */
public class AppConfig {
    // Server user login url
    public static String URL_LOGIN = "http://192.168.43.162/android_db/cd/reg.php";

    // Server user register url
    public static String URL_REGISTER = "http://10.0.2.2/android_api/cd/register.php";

   public static final String REGISTER_URL = "http://192.168.43.230/android_db/reg.php";
    public static final String LOGIN_URL = "http://192.168.43.230/android_db/login.php";
    public static final String BLOOD_GLUCOSE = "http://192.168.43.230/android_db/bloodglucose.php";
    public static final String BLOOD_PRESSURE="http://192.168.43.230/android_db/bloodpressure.php";
    public static final String DOCTOR_URL="http://192.168.43.230/Check/public/doctor";
    public static final String DOC_URL="http://192.168.43.230/Check/public/doc/";
    public static final String GET_CHAT="http://192.168.43.230/Check/public/message/";
    public static final String GET_VISIT="http://192.168.43.230/Check/public/getvisits/";
    public static final String GET_GLUCOSE="http://192.168.43.230/Check/public/getglucose/";

    public static final String GET_PRESSURE="http://192.168.43.230/Check/public/getpressure/";


    public static final String DELETE_GLUCOSE = "http://192.168.43.230/Check/public/deleteglucose";
    public static final String DELETE_PRESSURE = "http://192.168.43.230/Check/public/deletepressure";
    public static final String DELETE_VISIT = "http://192.168.43.230/Check/public/deletevisit";
    public static final String POST_CHAT="http://192.168.43.230/Check/public/receivechat";
    public static final String POST_EMERGENCY_INFO="http://192.168.43.230/Check/public/postemergency";
    public static final String POST_VISIT = "http://192.168.43.230/Check/public/postvisit";

    //    public static final String U_PROFILE="http://192.168.43.15/Check/public/uprofile";
public static final String U_PROFILE="http://192.168.43.230/Check/public/uprofile";
    public static final String KEY_USERNAME = "username";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_GLUCOSELEVEL="glucoselevel";
    public static final String KEY_GLUCOSEUSERID="glucoseuserid";
    public static final String KEY_GLUCOSEDATE="glucosedate";
    public static final String KEY_GLUCOSEID="glucoseid";
    public static final String KEY_PASWORD2="password2";
    public static final String KEY_USER="user_id";
    public static final String KEY_USER_DOC="user_doc";
////////
public static final String KEY_HEIGHTFEET="height_feet";
    public static final String KEY_HEIGHTINCHES="height_inches";
    public static final String KEY_WEIGHT="weight";
    public static final String KEY_TEMPERATURE="temperature";
    public static final String KEY_DIAGNOSIS="diagnosis";
    public static final String KEY_TREATMENT="treatment";
    public static final String KEY_PRESSURE="blood_pressure";
    public static final String KEY_VISITID="visit_id";



    ////////////////////////
    public static final String KEY_CHRONIC_ILLNESS="chronic_illness";
    public static  final String KEY_NEXT_0F_KIN="next_of_kin";
    public static final String KEY_NEXT_OF_KIN_CONTACT="next_of_kin_contact";
    public static final String KEY_ALLERGIES="allergies";
//patient_id,pressure_id,pressure_diastolic,pressure_systolic,pressure_pulse,pressure_date
    public static final String KEY_PATIENTID="patientid";

    public static final String KEY_PRESSUREID="pressureid";
    public static final String KEY_PRESSUREDIASTOLIC="pressurediastolic";
    public static final String KEY_PRESSURESYSTOLIC="pressuresystolic";
    public static final String KEY_PRESSUREPULSE="pressurepulse";
    public static final String KEY_PRESSUREDATE="pressuredate";
    public static final String KEY_MESSAGE="message";
    public static final String KEY_SENDERID="sender_id";
    public static final String KEY_RECEIVERID="receiver_id";
    public static final String KEY_CREATEDAT="created_at";
    public static final String KEY_GENDER="gender";
    public static final String KEY_NATIONALID="national_id";

    public static final String KEY_PHONENUMBER="phone_number";

}
