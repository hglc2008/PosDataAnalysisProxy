/**
 * Created at 2007-12-10 by pony
 */
package com.allinpay.util;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <pre>
 * ͨ���ַ���������
 *  
 * ������κζԴ�����޸�,�밴����ĸ�ʽע���޸ĵ�����. 
 * ���       ʱ��                              ����                  �޸����� 
 * 1.   2015-1-5    changlei update
 * </pre>
 */
public final class StringUtil {

	private static final String algorithm = "MD5";

	// �������������ʽ
	public static Pattern orderAmountPattern = Pattern.compile("\\d{1,16}");

	// ������������ʽ
	public static Pattern orderNoPattern = Pattern.compile("[\\w,_,-]{1,50}");

	// �ֻ����뱸��������ʽ�����ã�^(13[0-9]|15[0|3|6|7|8|9]|18[8|9])\\d{8}$
	public static Pattern mobileNoPattern = Pattern.compile("^(13[0-9]|15[0-9]|18[0-9])\\d{8}$");

	public static Pattern ratePattern = Pattern
			.compile("^((([1-9]{1}\\d{0,1}))|([0]{1}))((\\.(\\d){1,2}))?$|(100|100.0|100.00)");

	// email��ַ
	public static Pattern emailPattern = Pattern.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");

	// ��ʽ IP:PORT
	public static Pattern ipAndPortPattern = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}:\\d{1,5}");

	/**
	 * �ж�object�Ƿ�Ϊ��
	 * 
	 * @param object
	 *            Object����
	 * @return ����ֵ
	 */
	public static boolean isNull(Object object) {
		if (object instanceof String) {
			return StringUtil.isEmpty(String.valueOf(object));
		}
		return object == null;
	}

	/**
	 * �ж��ַ����Ƿ�Ϊ��.
	 * 
	 * @param src
	 * @return
	 */
	public static boolean isEmpty(final String src) {
		if (null == src || "".equals(src)) {
			return true;
		}
		return false;
	}

	public static String toString(final Object obj) {
		if (null == obj) {
			return null;
		} else {
			return String.valueOf(obj);
		}
	}

	/**
	 * ������ĸ��Ϊ��д.
	 * 
	 * @param src
	 * @return
	 */
	public static String capFirst(final String src) {
		if (isEmpty(src)) {
			return src;
		}
		if (src.length() == 1) {
			return src.toUpperCase();
		}
		String first = src.substring(0, 1);
		first = first.toUpperCase();
		return first + src.substring(1);
	}

	/**
	 * ������ĸ��ΪСд.
	 * 
	 * @param src
	 * @return
	 */
	public static String uncapFirst(final String src) {
		if (isEmpty(src)) {
			return src;
		}
		if (src.length() == 1) {
			return src.toLowerCase();
		}
		String first = src.substring(0, 1);
		first = first.toLowerCase();
		return first + src.substring(1);
	}

	/**
	 * Replace all occurances of a string within another string.
	 * 
	 * @param text
	 *            text to search and replace in
	 * @param repl
	 *            String to search for
	 * @param with
	 *            String to replace with
	 * @return the text with any replacements processed
	 * @see #replace(String text, String repl, String with, int max)
	 */
	public static String replace(String text, String repl, String with) {
		return replace(text, repl, with, -1);
	}

	/**
	 * Replace a string with another string inside a larger string, for the
	 * first <code>max</code> values of the search string. A <code>null</code>
	 * reference is passed to this method is a no-op.
	 * 
	 * @param text
	 *            text to search and replace in
	 * @param repl
	 *            String to search for
	 * @param with
	 *            String to replace with
	 * @param max
	 *            maximum number of values to replace, or <code>-1</code> if no
	 *            maximum
	 * @return the text with any replacements processed
	 * @throws NullPointerException
	 *             if repl is null
	 */
	public static String replace(String text, String repl, String with, int max) {
		if (text == null) {
			return null;
		}

		if (StringUtil.isEmpty(repl)) {
			throw new RuntimeException("���滻�ַ���Ϊ��!");
		}

		StringBuffer buf = new StringBuffer(text.length());
		int start = 0;
		int end = text.indexOf(repl, start);
		while (end != -1) {
			buf.append(text.substring(start, end)).append(with);
			start = end + repl.length();

			if (--max == 0) {
				break;
			}
			end = text.indexOf(repl, start);
		}
		buf.append(text.substring(start));
		return buf.toString();
	}

	/**
	 * ��Map�еı�����-����ֵ�滻Դ�ַ����еı�����. ��֧��$a${name}${value{}���Ƶ��滻.
	 * �������ֻ�����ϸ���ѭ${v}�ĸ�ʽ,����$,{,}�������ű�����Ϊ������. ���Ҫ�滻���ַ����а��������������ֵ���һ��,��˷���������ȷ����.
	 * 
	 * @param src
	 *            �ַ���
	 * @param value
	 *            ������-����ֵ
	 * @return String <br>
	 *         <br>
	 *         Example: <br>
	 *         String src = "Hello ${username}, this is ${target} speaking.";
	 *         <br>
	 *         Map map = new HashMap(); <br>
	 *         map.put("username", "petter"); <br>
	 *         map.put("target", "tom"); <br>
	 *         src = StringUtil.replaceVariable(str, map); <br>
	 *         #The src equals: <br>
	 *         "Hello petter, this is tom speaking."
	 */
	public static String replaceVariable(final String src, final Map value) {
		int len = src.length();
		StringBuffer buf = new StringBuffer(len);
		for (int i = 0; i < len; i++) {
			char c = src.charAt(i);
			if (c == '$') {
				i++;
				StringBuffer key = new StringBuffer();
				char temp = src.charAt(i);
				while (temp != '}') {
					if (temp != '{') {
						key.append(temp);
					}
					i++;
					temp = src.charAt(i);
				}
				String variable = (String) value.get(key.toString());
				if (null == variable) {
					buf.append("");
				} else {
					buf.append(variable);
				}
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	/**
	 * �Ѳ���src�ַ����еĴ�д�滻Ϊ_Сд. ����: NotificationEvent-->_notification_event.
	 * 
	 * @param src
	 *            String
	 * @return String
	 */
	public static String relaceCapitalWith_AndLowercase(String src) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);
			if (Character.isLowerCase(c)) {// Сд��ĸ.
				buf.append(c);
			} else if (Character.isUpperCase(c)) {// ��д��ĸ
				buf.append('_').append(Character.toLowerCase(c));
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	/**
	 * �Ѳ���src�ַ����е�_Сд�滻Ϊ��д. ����: _notification_event-->NotificationEvent
	 * 
	 * @param src
	 *            String
	 * @return String
	 */
	public static String replace_AndLowercaseWithCapital(String src) {
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);
			if ('_' == c) {
				i++;
				if (i < src.length() - 1) {
					c = src.charAt(i);
					buf.append(Character.toUpperCase(c));
				}
			} else {
				buf.append(c);
			}
		}
		return buf.toString();
	}

	/**
	 * ��key=value׷�ӵ�����/ǩ���ַ������.
	 * 
	 * @param buf
	 * @param key
	 * @param value
	 */
	public static void appendSignPara(StringBuffer buf, String key, String value) {
		if (!StringUtil.isEmpty(value)) {
			buf.append(key).append('=').append(value).append('&');
		}
	}

	/**
	 * ��key=value׷�ӵ�����/ǩ���ַ�����ĩβ.�ַ������ټ��������µ�key=value.
	 * 
	 * @param buf
	 * @param key
	 * @param value
	 */
	public static void appendLastSignPara(StringBuffer buf, String key, String value) {
		if (!StringUtil.isEmpty(value)) {
			buf.append(key).append('=').append(value);
		}
	}

	/**
	 * 
	 * @param buf
	 * @param key
	 * @param value
	 */
	public static void appendUrlPara(StringBuffer buf, String key, String value) {
		if (!StringUtil.isEmpty(value)) {
			try {
				buf.append(key).append('=').append(URLEncoder.encode(value, "utf-8")).append('&');
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 
	 * @param buf
	 * @param key
	 * @param value
	 */
	public static void appendLastUrlPara(StringBuffer buf, String key, String value) {
		if (!StringUtil.isEmpty(value)) {
			try {
				buf.append(key).append('=').append(URLEncoder.encode(value, "utf-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * ������KEYֵ���鵽��ַ����
	 * 
	 * @param buf
	 * @param key
	 * @param value
	 * @author nilomiao
	 * @since 2010-5-24
	 */
	public static void appendUrlParaForGetting(StringBuffer buf, String key, String value) {
		if (!StringUtil.isEmpty(value)) {
			try {
				buf.append(key).append('=').append(URLEncoder.encode(value, "utf-8")).append('&');
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			buf.append(key).append('=').append('&');
		}
	}

	/**
	 * ���ַ���src��䵽len���ȣ������ַ���Ϊpadding����䷽��Ϊ����stuffHeadΪtrueʱ ��䵽srcͷ����������䵽β��.
	 * 
	 * @param src
	 * @param len
	 * @param stuffHead
	 * @param padding
	 */
	public static String stuffString(String src, int len, boolean stuffHead, char padding) {
		if (len <= 0) {
			return src;
		}
		if (isEmpty(src)) {
			src = "";
		}
		int srcLen = src.length();
		StringBuffer buf = new StringBuffer(len);
		int paddingLen = len - srcLen;
		for (int i = 0; i < paddingLen; i++) {
			buf.append(padding);
		}
		if (stuffHead) {
			buf.append(src);
		} else {
			buf.insert(0, src);
		}
		return buf.toString();
	}

	/**
	 * ������data��ǰ�����������data�ĳ��� ���ȸ�ʽΪ����λ����������λ�Ĳ�0:14->14,8->08
	 * 
	 * @param data
	 * @return
	 */
	public static String padDataWithLen(String data) {
		int len = data.length();
		String lenStr = String.valueOf(len);
		if (len < 10) {
			lenStr = "0" + lenStr;
		}
		return lenStr + data;
	}

	/**
	 * �ж��ַ����Ƿ�ȫ����
	 * 
	 * @param data
	 * @return
	 */
	public static boolean isNumber(String data) {

		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(data);
		return isNum.matches();
	}

	/**
	 * �ж��ַ����Ƿ����ת��ΪInteger
	 * 
	 * @author Angi Wang
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isInteger(String str) {
		try {
			Integer.parseInt(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * �ж��ַ����Ƿ����ת��ΪLong
	 * 
	 * @author Angi Wang
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isLong(String str) {
		try {
			Long.parseLong(str);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	/**
	 * �жϽ���ʽ����λС����
	 * 
	 */
	public static boolean isMoney(String money) {

		String[] tmpMoney = money.split("[.]");
		String tmp = "";
		for (int i = 0; i < tmpMoney.length; i++) {
			tmp += tmpMoney[i];
		}

		if (!isNumber(tmp)) {
			return false;
		}
		if (!money.contains(".")) {
			return false;
		} else {
			for (int i = 0; i < tmpMoney.length; i++) {
				if (i == 1) {
					tmp = "";
					tmp += tmpMoney[i];
				}

			}
			if (tmp.length() != 2) {
				return false;
			}
		}
		return true;
	}

	public static String parseNotifyResponse(String notifyMerchantResponse) {
		if (StringUtil.isEmpty(notifyMerchantResponse)) {
			return null;
		}
		int index1 = notifyMerchantResponse.indexOf("<pickupUrl>");
		if (index1 < 0) {
			return null;
		}
		int index2 = notifyMerchantResponse.indexOf("</pickupUrl>", index1);
		if (index2 < 0) {
			return null;
		}
		index1 += "<pickupUrl>".length();
		return notifyMerchantResponse.substring(index1, index2);
	}

	public static String stringTrim(String value) {
		if ("null".equals(value)) {
			return null;
		} else {
			return value.trim();
		}
	}

	public static String fillLLVAR(String str) {
		if (str == null || "null".equals(str))
			return null;

		String lenTemp = "";
		int length = str.length();
		if (length == 0)
			return "00";
		if (length < 10)
			lenTemp = "0";
		if (length >= 10 && length < 100)
			lenTemp = "";

		StringBuffer sb = new StringBuffer("").append(lenTemp).append(length).append(str);
		return sb.toString();

	}

	public static String fillLLLVAR(String str) {
		if (str == null || "null".equals(str))
			return null;

		String lenTemp = "";
		int length = str.length();
		if (length == 0)
			return "000";
		if (length < 10)
			lenTemp = "00";
		if (length >= 10 && length < 100)
			lenTemp = "0";
		if (length >= 100 && length < 1000)
			lenTemp = "";

		StringBuffer sb = new StringBuffer("").append(lenTemp).append(length).append(str);
		return sb.toString();

	}

	/**
	 * ����ַ���
	 * 
	 * @param string
	 *            ������ַ���
	 * @param filler
	 *            ����ַ�
	 * @param totalLength
	 *            �����ܳ���
	 * @param atEnd
	 *            �ַ���ǰ��������־λ
	 * @return
	 */
	public static String fillString(String string, char filler, int totalLength, boolean atEnd) {
		byte[] tempbyte = null;
		try {
			tempbyte = string.getBytes("gbk");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "";
		}
		int currentLength = tempbyte.length;
		int delta = totalLength - currentLength;
		for (int i = 0; i < delta; i++) {
			if (atEnd) {
				string += filler;
			} else {
				string = filler + string;
			}
		}
		return string;
	}

	/**
	 * ȡ��XML�ı��ı�ǩ����
	 * 
	 * @param String
	 *            strXML
	 * @param String
	 *            tagName ��ǩ����
	 * @return String ��ǩֵ
	 * @author nilomiao
	 * @since 2009-12-22
	 */
	public static String getTagValue(String strXML, String tagName) {
		if (null == tagName || "".equals(tagName)) {
			return "";
		}
		String startTag = "<" + tagName + ">";
		String endTag = "</" + tagName + ">";
		if (null == strXML || strXML.indexOf(startTag) == -1 || strXML.indexOf(endTag) == -1) {
			return "";
		}

		return strXML.substring(strXML.indexOf(startTag) + startTag.length(), strXML.indexOf(endTag));
	}

	/**
	 * ȡ��XML�ı��ı�ǩ���ݣ�������ǩ
	 * 
	 * @param String
	 *            strXML
	 * @param String
	 *            tagName ��ǩ����
	 * @return String ��ǩֵ
	 * @author nilomiao
	 * @since 2012-2-2
	 */
	public static String getTagNameAndValue(String strXML, String tagName) {
		if (null == tagName || "".equals(tagName)) {
			return "";
		}
		String startTag = "<" + tagName + ">";
		String endTag = "</" + tagName + ">";
		if (null == strXML || strXML.indexOf(startTag) == -1 || strXML.indexOf(endTag) == -1) {
			return "";
		}
		return strXML.substring(strXML.indexOf(startTag), strXML.indexOf(endTag) + endTag.length());
	}

	/**
	 * �����ַ����ֽڳ��ȣ�GBK���룩
	 * 
	 * @param data
	 * @return
	 */
	public static int getBytes(String data) {
		byte[] tempbyte = null;
		try {
			tempbyte = data.getBytes("gbk");
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
		return tempbyte.length;
	}

	/**
	 * �ж��ַ����Ƿ񳬹�����ֽ�����GBK���룩
	 * 
	 * @param data
	 * @param maxLength
	 * @return false ���� true δ����
	 */
	public static boolean checkLength(String data, int maxLength) {
		if (getBytes(data) > maxLength)
			return false;
		return true;
	}

	/**
	 * �������
	 * 
	 * @param password
	 * @return
	 */
	public static String encodePassword(String password) {
		byte[] unencodedPassword = password.getBytes();

		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance(algorithm);
		} catch (Exception e) {
			e.printStackTrace();
		}
		md.reset();
		// call the update method one or more times
		// (useful when you don't know the size of your data, eg. stream)
		md.update(unencodedPassword);
		// now calculate the hash
		byte[] encodedPassword = md.digest();
		StringBuffer buf = new StringBuffer();
		for (int i = 0; i < encodedPassword.length; i++) {
			if ((encodedPassword[i] & 0xff) < 0x10) {
				buf.append("0");
			}
			buf.append(Long.toString(encodedPassword[i] & 0xff, 16));
		}
		return buf.toString();

	}

	/**
	 * �ж�һ���ַ����Ƿ����ƶ���ģʽƥ��
	 * 
	 * @param p
	 * @param matherStr
	 * @return
	 */
	public static boolean isPatternMatcher(final Pattern p, final String matherStr) {
		Matcher m = p.matcher(matherStr);
		return m.matches();
	}

	/**
	 * �õ��Ʒ����ĳ��ȣ����İ�2���ֽڼ���
	 * 
	 * @param s
	 * @return
	 */
	public static int getStrLength(final String src) {
		String temp = src.replaceAll("[^\\x00-\\xff]", "**");
		int length = temp.length();
		return length;
	}

	/**
	 * ���ض���ʽ���ַ��� ת�� ��MAP���� ��ʽΪ��key=value|key1=value1...
	 * 
	 * @param content
	 * @return
	 */
	public static Map<String, String> convertForStrToMap(String content) {
		Map<String, String> result = new HashMap<String, String>();

		String[] params = content.split("\\|");

		String[] tmp = null;
		for (String param : params) {
			tmp = param.split("=");

			if (tmp.length == 2) {
				result.put(tmp[0], tmp[1]);
			} else {
				result.put(tmp[0], "");
			}
		}

		return result;
	}

	/**
	 * �õ��Ʒ������ֽڳ��ȣ������ַ���1���ֽ�;���İ�2���ֽڼ���
	 * 
	 * @param s
	 * @return
	 */
	public static int getByteLength(final String src) {
		String temp = src.replaceAll("[^\\x00-\\xff]", "**");
		int length = temp.length();
		return length;
	}

	/**
	 * <pre>
	 * �ַ�����������ʽ��subString����jdk apiһ��������ͷ������β
	 * ������������һ�����ֲ���ȫ�����
	 * 
	 * &#64;param src 
	 * &#64;param beginIndex ��ʼ����(�ֽ�����)
	 * &#64;param endIndex  ��������(�ֽ�����)
	 * &#64;return
	 * </pre>
	 */
	public static String subStringByte(final String src, int beginIndex, int endIndex) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0, ix = 0; i < src.length() && ix < endIndex; i++) {
			char c = src.charAt(i);
			if (ix >= beginIndex && ix < endIndex) {
				buffer.append(c);
			}
			boolean flag = false;
			try {
				flag = isChineseChar(c);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			if (flag) {
				ix += 2;
			} else {
				ix++;
			}
		}
		return buffer.toString();
	}

	/**
	 * �ж��Ƿ���һ�����ĺ���
	 * 
	 * @param c
	 *            �ַ�
	 * @return true��ʾ�����ĺ��֣�false��ʾ��Ӣ����ĸ
	 * @throws UnsupportedEncodingException
	 *             ʹ����JAVA��֧�ֵı����ʽ
	 */
	private static boolean isChineseChar(char c) throws UnsupportedEncodingException {
		// ����ֽ�������1���Ǻ���
		// �����ַ�ʽ����Ӣ����ĸ�����ĺ��ֲ�����ʮ���Ͻ������������Ŀ�У������ж��Ѿ��㹻��
		return String.valueOf(c).getBytes("GBK").length > 1;
	}
}
