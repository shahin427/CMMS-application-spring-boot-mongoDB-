package org.sayar.net.Tools;

public class PatternList {
	public final static String faAndEnText = "^[a-zA-z آ-ی]*";
	public final static String email = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
	public final static String phone = "(([0]+[9])([0-9]{9})||([۰]+[۹])([۰-۹]{9}))$";
	public final static String tell = "(([0])([0-9]{10})||([۰])([۰-۹]{10}))$";
	public final static String nationalCode = "(([0-9]{10})||([۰-۹]{10}))$";
	public final static String faText = "[آ-ی ]+";
	public final static String date = "^(([0-9]{1})([.,/]([0-9]{1,2})){0,1}|([۰-۹]{1})([.,/]([۰-۹]{1,2})){0,1})";
	public final static String streetName = "[۰-۹-0-9آ-ی ]+";
	public final static String bankCard = "((([1-9])([0-9]{18})||([۱-۹])([۰-۹]{18}))||(([1-9])([0-9]{15})||([۱-۹])([۰-۹]{15})))$";
	public final static String bcNumber = "[1-9]{1}|[1-9][0-9]{2}|[۱-۹]{1}|[۱-۹][۰-۹]{2}]"; // شماره شناسنامه
	public final static String password = "[-_/@!#$%^&*().A-Za-z0-9]+";
	public final static String number = "[0-9]|[۰-۹]";
	public final static String digitNumber = "^[0-9]*$";
	public final static String numberNotZero = "[1-9]|[۱-۹]";
	public final static String faNumberAndText = "[۰-۹]*|[آ-ی ]*";
	public final static String postalCode = "((([1-9]{1})([0-9]{9}))||(([۱-۹]{1})([۰-۹]{9})))$";
	public final static String dateInModel = "MM/dd/yyyy HH:mm:ss";
	public final static String courseCode = "[a-zA-Z0-9]{8}$";
	public final static String objectId = "^[0-9a-fA-F]{24}$";
	public final static String USERNAME = "^[a-zA-Z0-9_-]{4,15}$";
}
