package com.enov.bel.core.utils

import android.os.Environment
import java.io.*
import java.net.URLEncoder
import java.util.*

/**
 * @Description  {具体做什么}
 * @Author wushan
 * @Date 2021/12/9 22:14
 */
class CodeUtil {
    companion object {
        //java 合并两个byte数组
        fun byteMerger(byte_1: ByteArray, byte_2: ByteArray): ByteArray {
            val byte_3 = ByteArray(byte_1.size + byte_2.size)
            System.arraycopy(byte_1, 0, byte_3, 0, byte_1.size)
            System.arraycopy(byte_2, 0, byte_3, byte_1.size, byte_2.size)
            return byte_3
        }

        fun getErrorXml(code: Int): String {
            return "<error_code>$code</error_code>"
        }

        fun bytesReverse2Int(array: ByteArray): Int {
            val hexStr = bytes2HexStringReverse(array)
            val devStr = hexToDec(hexStr)
            return devStr.toInt()
        }

        /** 用于打印输出 测试用
         * 字节数组转16进制字符串
         */
        fun bytes2HexString(array: ByteArray): String {
            val builder = StringBuilder()
            for (b in array) {
                var hex = Integer.toHexString(b.toInt() and 0xFF)
                if (hex.length == 1) {
                    hex = "0$hex"
                }
                builder.append(hex)
            }
            return builder.toString().uppercase(Locale.getDefault())
        }

        fun bytes2HexStringReverse(array: ByteArray): String {
            val builder = StringBuilder()
            for (i in array.indices.reversed()) {
                var hex = Integer.toHexString(array[i].toInt() and 0xFF)
                if (hex.length == 1) {
                    hex = "0$hex"
                }
                builder.append(hex)
            }
            return builder.toString().uppercase(Locale.getDefault())
        }

        /**
         * Get XML String of utf-8
         *
         * @return XML-Formed string
         */
        fun getUTF8XMLString(xml: String?): String {
            // A StringBuffer Object
            val sb = StringBuffer()
            sb.append(xml)
            var xmString = ""
            var xmlUTF8 = ""
            try {
                xmString = String(sb.toString().toByteArray(charset("UTF-8")))
                xmlUTF8 = URLEncoder.encode(xmString, "UTF-8")
            } catch (e: UnsupportedEncodingException) {
                e.printStackTrace()
            }
            // return to String Formed
            return xmlUTF8
        }
        /**
         * int ->16str
         * @param value
         * @param length
         * @return
         */
        /**
         * int -> 16str
         * @param value
         * @return
         */
        @JvmOverloads
        fun int2Hex(value: Int, length: Int = 2): String {
            var st = String.format(
                "%" + length + "s",
                Integer.toHexString(value).uppercase(Locale.getDefault())
            )
            st = st.replace(" ".toRegex(), "0")
            return st
        }

        /**
         * 将16进制字符串转换为byte[]
         *
         * @param str
         * @return
         */
        fun toBytes(str: String?): ByteArray {
            if ((str == null) || str.trim { it <= ' ' } == "") {
                return ByteArray(0)
            }
            val bytes = ByteArray(str.length / 2)
            for (i in 0 until str.length / 2) {
                val subStr = str.substring(i * 2, i * 2 + 2)
                bytes[i] = subStr.toInt(16).toByte()
            }
            return bytes
        }

        /**
         * 字符串转化成为16进制字符串
         *
         * @param s
         * @return
         */
        fun strTo16(s: String): String {
//        String str = "";
//        for (int i = 0; i < s.length(); i++) {
//            int ch = (int) s.charAt(i);
//            String s4 = Integer.toHexString(ch);
//            str = str + s4;
//        }
//        return str.toUpperCase();
            val chars = "0123456789ABCDEF".toCharArray()
            val sb = StringBuilder("")
            val bs = s.toByteArray()
            var bit: Int
            for (i in bs.indices) {
                bit = bs[i].toInt() and 0x0f0 shr 4
                sb.append(chars[bit])
                bit = bs[i].toInt() and 0x0f
                sb.append(chars[bit])
                // sb.append(' ');
            }
            return sb.toString().trim { it <= ' ' }
        }

        /**
         * 16进制转换成为string类型字符串
         * @param hexStr
         * @return
         */
        fun hexStringToString(hexStr: String): String {
//        if (s == null || s.equals("")) {
//            return null;
//        }
//        s = s.replace(" ", "");
//        byte[] baKeyword = new byte[s.length() / 2];
//        for (int i = 0; i < baKeyword.length; i++) {
//            try {
//                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//        try {
//            s = new String(baKeyword, "UTF-8");
//            new String();
//        } catch (Exception e1) {
//            e1.printStackTrace();
//        }
//        return s;
            val str = "0123456789ABCDEF"
            val hexs = hexStr.toCharArray()
            val bytes = ByteArray(hexStr.length / 2)
            var n: Int
            for (i in bytes.indices) {
                n = str.indexOf(hexs[2 * i]) * 16
                n += str.indexOf(hexs[2 * i + 1])
                bytes[i] = (n and 0xff).toByte()
            }
            return String(bytes)
        }

        fun hexToDec(hexStr: String): String {
            /*String str;
            if (hexStr.equals("00000000")) {
                str = hexStr;
            } else {
                str = hexStr.replaceFirst("^0*", "");
            }*/
            val data = hexStr.toInt(16)
            return Integer.toString(data, 10)
        }

        /**
         * 16进制字符串转byte数组
         * @param hex
         * @return
         */
        fun hexToByte(hex: String): ByteArray {
            var m = 0
            var n = 0
            val byteLen = hex.length / 2 // 每两个字符描述一个字节
            val ret = ByteArray(byteLen)
            for (i in 0 until byteLen) {
                m = i * 2 + 1
                n = m + 1
                val intVal = Integer.decode("0x" + hex.substring(i * 2, m) + hex.substring(m, n))
                ret[i] = java.lang.Byte.valueOf(intVal.toByte())
            }
            return ret
        }

        /**
         * 将int转为高字节在前，低字节在后的byte数组（大端）
         *
         * @param n int
         * @return byte[]
         */
        fun intToByteBig(n: Int): ByteArray {
            val b = ByteArray(4)
            b[3] = (n and 0xff).toByte()
            b[2] = (n shr 8 and 0xff).toByte()
            b[1] = (n shr 16 and 0xff).toByte()
            b[0] = (n shr 24 and 0xff).toByte()
            return b
        }

        /**
         * 将int转为低字节在前，高字节在后的byte数组（小端）
         *
         * @param n int
         * @return byte[]
         */
        fun intToByteLittle(n: Int): ByteArray {
            val b = ByteArray(4)
            b[0] = (n and 0xff).toByte()
            b[1] = (n shr 8 and 0xff).toByte()
            b[2] = (n shr 16 and 0xff).toByte()
            b[3] = (n shr 24 and 0xff).toByte()
            return b
        }

        /** float转byte(小端序)  */
        fun float2ByteLittle(big: Float): ByteArray {
            val data = java.lang.Float.floatToIntBits(big)
            return intToByteLittle(data)
        }

        /**
         * byte数组到int的转换(小端)
         *
         * @param bytes
         * @return
         */
        fun bytes2IntLittle(bytes: ByteArray): Int {
            val int1 = bytes[0].toInt() and 0xff
            val int2 = bytes[1].toInt() and 0xff shl 8
            val int3 = bytes[2].toInt() and 0xff shl 16
            val int4 = bytes[3].toInt() and 0xff shl 24
            return int1 or int2 or int3 or int4
        }

        /**
         * byte数组到int的转换(大端)
         *
         * @param bytes
         * @return
         */
        fun bytes2IntBig(bytes: ByteArray): Int {
            val int1 = bytes[3].toInt() and 0xff
            val int2 = bytes[2].toInt() and 0xff shl 8
            val int3 = bytes[1].toInt() and 0xff shl 16
            val int4 = bytes[0].toInt() and 0xff shl 24
            return int1 or int2 or int3 or int4
        }

        /**
         * 将short转为高字节在前，低字节在后的byte数组（大端）
         *
         * @param n short
         * @return byte[]
         */
        fun shortToByteBig(n: Short): ByteArray {
            val b = ByteArray(2)
            b[1] = (n.toInt() and 0xff).toByte()
            b[0] = (n.toInt() shr 8 and 0xff).toByte()
            return b
        }

        /**
         * 将short转为低字节在前，高字节在后的byte数组(小端)
         *
         * @param n short
         * @return byte[]
         */
        fun shortToByteLittle(n: Short): ByteArray {
            val b = ByteArray(2)
            b[0] = (n.toInt() and 0xff).toByte()
            b[1] = (n.toInt() shr 8 and 0xff).toByte()
            return b
        }

        /**
         * 读取小端byte数组为short
         *
         * @param b
         * @return
         */
        fun byteToShortLittle(b: ByteArray): Short {
            return (b[1].toInt() shl 8 or (b[0].toInt() and 0xff)).toShort()
        }

        /**
         * 读取大端byte数组为short
         *
         * @param b
         * @return
         */
        fun byteToShortBig(b: ByteArray): Short {
            return (b[0].toInt() shl 8 or (b[1].toInt() and 0xff)).toShort()
        }

        /**
         * long类型转byte[] (大端)
         *
         * @param n
         * @return
         */
        fun longToBytesBig(n: Long): ByteArray {
            val b = ByteArray(8)
            b[7] = (n and 0xffL).toByte()
            b[6] = (n shr 8 and 0xffL).toByte()
            b[5] = (n shr 16 and 0xffL).toByte()
            b[4] = (n shr 24 and 0xffL).toByte()
            b[3] = (n shr 32 and 0xffL).toByte()
            b[2] = (n shr 40 and 0xffL).toByte()
            b[1] = (n shr 48 and 0xffL).toByte()
            b[0] = (n shr 56 and 0xffL).toByte()
            return b
        }

        /**
         * long类型转byte[] (小端)
         *
         * @param n
         * @return
         */
        fun longToBytesLittle(n: Long): ByteArray {
            val b = ByteArray(8)
            b[0] = (n and 0xffL).toByte()
            b[1] = (n shr 8 and 0xffL).toByte()
            b[2] = (n shr 16 and 0xffL).toByte()
            b[3] = (n shr 24 and 0xffL).toByte()
            b[4] = (n shr 32 and 0xffL).toByte()
            b[5] = (n shr 40 and 0xffL).toByte()
            b[6] = (n shr 48 and 0xffL).toByte()
            b[7] = (n shr 56 and 0xffL).toByte()
            return b
        }

        /**
         * byte[]转long类型(小端)
         *
         * @param array
         * @return
         */
        fun bytesToLongLittle(array: ByteArray): Long {
            return (array[0].toLong() and 0xffL shl 0
                    or (array[1].toLong() and 0xffL shl 8)
                    or (array[2].toLong() and 0xffL shl 16)
                    or (array[3].toLong() and 0xffL shl 24)
                    or (array[4].toLong() and 0xffL shl 32)
                    or (array[5].toLong() and 0xffL shl 40)
                    or (array[6].toLong() and 0xffL shl 48)
                    or (array[7].toLong() and 0xffL shl 56))
        }

        /**
         * byte[]转long类型(大端)
         *
         * @param array
         * @return
         */
        fun bytesToLongBig(array: ByteArray): Long {
            return (array[0].toLong() and 0xffL shl 56
                    or (array[1].toLong() and 0xffL shl 48)
                    or (array[2].toLong() and 0xffL shl 40)
                    or (array[3].toLong() and 0xffL shl 32)
                    or (array[4].toLong() and 0xffL shl 24)
                    or (array[5].toLong() and 0xffL shl 16)
                    or (array[6].toLong() and 0xffL shl 8)
                    or (array[7].toLong() and 0xffL shl 0))
        }

        /**
         * 这个是把文件变成二进制流
         *
         * @return
         * @throws Exception
         */
        @Throws(Exception::class)
        fun readStream(): ByteArray {

//        String fileName = FILE_MODEL_DATA_DIR + "baowen.log";
            val fileName = Environment.getExternalStorageDirectory().toString() +
                    File.separator + "datasend.log"
            val fs = FileInputStream(fileName)
            val outStream = ByteArrayOutputStream()
            val buffer = ByteArray(1024)
            var len = 0
            while (-1 != fs.read(buffer).also { len = it }) {
                outStream.write(buffer, 0, len)
            }
            outStream.close()
            fs.close()
            return outStream.toByteArray()
        }

        fun saveAsFileWriter(content: String?, path: String?) {
            var fwriter: FileWriter? = null
            try {
                // true表示不覆盖原来的内容，而是加到文件的后面。若要覆盖原来的内容，直接省略这个参数就好
                fwriter = FileWriter(path, true)
                fwriter.write(content)
            } catch (ex: IOException) {
                ex.printStackTrace()
            } finally {
                try {
                    fwriter!!.flush()
                    fwriter.close()
                } catch (ex: IOException) {
                    ex.printStackTrace()
                }
            }
        }
    }
}