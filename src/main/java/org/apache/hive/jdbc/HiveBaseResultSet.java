//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package org.apache.hive.jdbc;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import org.apache.hadoop.hive.common.type.HiveDate;
import org.apache.hadoop.hive.common.type.HiveDecimal;
import org.apache.hadoop.hive.common.type.HiveIntervalDayTime;
import org.apache.hadoop.hive.common.type.HiveIntervalYearMonth;
import org.apache.hadoop.hive.common.type.HiveTime;
import org.apache.hive.service.cli.TableSchema;
import org.apache.hive.service.cli.TypeDescriptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class HiveBaseResultSet implements ResultSet {
    public static Logger logger = LoggerFactory.getLogger(HiveBaseResultSet.class);
    protected Statement statement = null;
    protected SQLWarning warningChain = null;
    protected boolean wasNull = false;
    protected Object[] row;
    protected List<String> columnNames;
    protected List<String> columnTypes;
    protected List<JdbcColumnAttributes> columnAttributes;
    private TableSchema schema;
    private long lastAccessTime;
    private int fetchDirection = 1000;

    public HiveBaseResultSet() {
    }

    public boolean absolute(int row) throws SQLException {
        logger.trace("{], {}", this.traceInfo(), row);
        throw new SQLException("Method not supported");
    }

    public void afterLast() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void beforeFirst() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void cancelRowUpdates() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void deleteRow() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public int findColumn(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        int columnIndex = this.columnNames.indexOf(columnName.toLowerCase());
        if (columnIndex == -1) {
            columnIndex = this.columnNames.indexOf(columnName);
            if (columnIndex == -1) {
                throw new SQLException();
            }
        }

        ++columnIndex;
        return columnIndex;
    }

    public boolean first() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public Array getArray(int i) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), i);
        Object obj = this.getObject(i);
        if (obj == null) {
            return null;
        } else if (obj instanceof Array) {
            return (Array)obj;
        } else {
            throw new SQLException("No Array found!");
        }
    }

    public Array getArray(String colName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), colName);
        return this.getArray(this.findColumn(colName));
    }

    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public InputStream getAsciiStream(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        throw new SQLException("Method not supported");
    }

    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object val = this.getObject(columnIndex);
        if (val != null && !(val instanceof BigDecimal)) {
            if (val instanceof HiveDecimal) {
                return ((HiveDecimal)val).bigDecimalValue();
            } else if (Number.class.isInstance(val)) {
                return new BigDecimal(val.toString());
            } else {
                throw new SQLException("Illegal conversion");
            }
        } else {
            return (BigDecimal)val;
        }
    }

    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getBigDecimal(this.findColumn(columnName));
    }

    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex + ", " + scale);
        MathContext mc = new MathContext(scale);
        return this.getBigDecimal(columnIndex).round(mc);
    }

    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName + ", " + scale);
        return this.getBigDecimal(this.findColumn(columnName), scale);
    }

    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else if (obj instanceof InputStream) {
            return (InputStream)obj;
        } else {
            ByteArrayInputStream is;
            if (obj instanceof byte[]) {
                byte[] byteArray = (byte[])((byte[])obj);
                is = new ByteArrayInputStream(byteArray);
                return is;
            } else if (obj instanceof String) {
                String str = (String)obj;
                is = null;

                try {
                    is = new ByteArrayInputStream(str.getBytes("UTF-8"));
                    return is;
                } catch (UnsupportedEncodingException var6) {
                    throw new SQLException("Illegal conversion to binary stream from column " + columnIndex + " - Unsupported encoding exception");
                }
            } else {
                throw new SQLException("Illegal conversion to binary stream from column " + columnIndex);
            }
        }
    }

    public InputStream getBinaryStream(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getBinaryStream(this.findColumn(columnName));
    }

    public Blob getBlob(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else if (obj instanceof byte[]) {
            return new HiveBlob((byte[])((byte[])obj));
        } else {
            throw new SQLException("Blob only fetch byte[] data byte");
        }
    }

    public Blob getBlob(String colName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), colName);
        return this.getBlob(this.findColumn(colName));
    }

    public boolean getBoolean(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (Boolean.class.isInstance(obj)) {
            return (Boolean)obj;
        } else if (obj == null) {
            return false;
        } else if (Number.class.isInstance(obj)) {
            return ((Number)obj).intValue() != 0;
        } else if (String.class.isInstance(obj)) {
            return !((String)obj).equals("0");
        } else {
            throw new SQLException("Cannot convert column " + columnIndex + " to boolean");
        }
    }

    public boolean getBoolean(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getBoolean(this.findColumn(columnName));
    }

    public byte getByte(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (Number.class.isInstance(obj)) {
            return ((Number)obj).byteValue();
        } else if (obj == null) {
            return 0;
        } else {
            throw new SQLException("Cannot convert column " + columnIndex + " to byte");
        }
    }

    public byte getByte(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getByte(this.findColumn(columnName));
    }

    public byte[] getBytes(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (obj instanceof byte[]) {
            return (byte[])((byte[])obj);
        } else {
            if (String.class.isInstance(obj)) {
                try {
                    return ((String)obj).getBytes("UTF-8");
                } catch (UnsupportedEncodingException var4) {
                    var4.printStackTrace();
                }
            } else if (obj == null) {
                return null;
            }

            throw new SQLException("Cannot convert column " + columnIndex + " to byte[]");
        }
    }

    public byte[] getBytes(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getBytes(this.findColumn(columnName));
    }

    public Reader getCharacterStream(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public Reader getCharacterStream(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getCharacterStream(this.findColumn(columnName));
    }

    public Clob getClob(int i) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), i);
        throw new SQLException("Method not supported");
    }

    public Clob getClob(String colName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), colName);
        return this.getClob(this.findColumn(colName));
    }

    public int getConcurrency() throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), 1007);
        return 1007;
    }

    public String getCursorName() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public Date getDate(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else if (obj instanceof Date) {
            return (Date)obj;
        } else if (obj instanceof HiveDate) {
            return new Date(((HiveDate)obj).getTime());
        } else if (obj instanceof Timestamp) {
            return new Date(((Timestamp)obj).getTime());
        } else if (obj instanceof String) {
            return this.toDate((String)obj);
        } else {
            throw new SQLException("Illegal conversion");
        }
    }

    public Date getDate(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getDate(this.findColumn(columnName));
    }

    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        return getDate(columnIndex);
    }

    public Date getDate(String columnName, Calendar cal) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getDate(this.findColumn(columnName), cal);
    }

    public double getDouble(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);

        try {
            Object obj = this.getObject(columnIndex);
            if (Number.class.isInstance(obj)) {
                return ((Number)obj).doubleValue();
            } else if (obj == null) {
                return 0.0;
            } else if (String.class.isInstance(obj)) {
                return Double.parseDouble((String)obj);
            } else {
                throw new Exception("Illegal conversion");
            }
        } catch (Exception var3) {
            throw new SQLException("Cannot convert column " + columnIndex + " to double: " + var3.toString(), var3);
        }
    }

    public double getDouble(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getDouble(this.findColumn(columnName));
    }

    public int getFetchDirection() throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), this.fetchDirection);
        return this.fetchDirection;
    }

    public int getFetchSize() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public float getFloat(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);

        try {
            Object obj = this.getObject(columnIndex);
            if (Number.class.isInstance(obj)) {
                return ((Number)obj).floatValue();
            } else if (obj == null) {
                return 0.0F;
            } else if (String.class.isInstance(obj)) {
                return Float.parseFloat((String)obj);
            } else {
                throw new Exception("Illegal conversion");
            }
        } catch (Exception var3) {
            throw new SQLException("Cannot convert column " + columnIndex + " to float: " + var3.toString(), var3);
        }
    }

    public float getFloat(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getFloat(this.findColumn(columnName));
    }

    public int getHoldability() throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), 1);
        return 1;
    }

    public int getInt(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);

        try {
            Object obj = this.getObject(columnIndex);
            if (Number.class.isInstance(obj)) {
                return ((Number)obj).intValue();
            } else if (obj == null) {
                return 0;
            } else if (String.class.isInstance(obj)) {
                return Integer.parseInt((String)obj);
            } else {
                throw new Exception("Illegal conversion");
            }
        } catch (Exception var3) {
            throw new SQLException("Cannot convert column " + columnIndex + " to integer" + var3.toString(), var3);
        }
    }

    public int getInt(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getInt(this.findColumn(columnName));
    }

    public long getLong(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);

        try {
            Object obj = this.getObject(columnIndex);
            if (Number.class.isInstance(obj)) {
                return ((Number)obj).longValue();
            } else if (obj == null) {
                return 0L;
            } else if (String.class.isInstance(obj)) {
                return Long.parseLong((String)obj);
            } else {
                throw new Exception("Illegal conversion");
            }
        } catch (Exception var3) {
            throw new SQLException("Cannot convert column " + columnIndex + " to long: " + var3.toString(), var3);
        }
    }

    public long getLong(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getLong(this.findColumn(columnName));
    }

    public ResultSetMetaData getMetaData() throws SQLException {
        logger.trace("{}", this.traceInfo());
        return new HiveResultSetMetaData(this.columnNames, this.columnTypes, this.columnAttributes);
    }

    public Reader getNCharacterStream(int arg0) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), arg0);
        throw new SQLException("Method not supported");
    }

    public Reader getNCharacterStream(String arg0) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), arg0);
        throw new SQLException("Method not supported");
    }

    public NClob getNClob(int arg0) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), arg0);
        throw new SQLException("Method not supported");
    }

    public NClob getNClob(String columnLabel) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnLabel);
        throw new SQLException("Method not supported");
    }

    public String getNString(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public String getNString(String columnLabel) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnLabel);
        throw new SQLException("Method not supported");
    }

    private Object getColumnValue(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        if (this.row == null) {
            throw new SQLException("No row found.");
        } else if (this.row.length == 0) {
            throw new SQLException("RowSet does not contain any columns!");
        } else if (columnIndex > this.row.length) {
            throw new SQLException("Invalid columnIndex: " + columnIndex);
        } else {
            TypeDescriptor columnType = this.getSchema().getColumnDescriptorAt(columnIndex - 1).getTypeDescriptor();

            try {
                Object evaluated = this.evaluate(columnType, this.row[columnIndex - 1]);
                this.wasNull = evaluated == null;
                return evaluated;
            } catch (Exception var4) {
                var4.printStackTrace();
                throw new SQLException("Unrecognized column type:" + columnType, var4);
            }
        }
    }

    private Object evaluate(TypeDescriptor typeDescriptor, Object value) throws SQLException {
        if (value == null) {
            return null;
        } else {
            BigDecimal result;
            switch (typeDescriptor.getType()) {
                case BINARY_TYPE:
                    if (value instanceof String) {
                        try {
                            return ((String)value).getBytes("UTF-8");
                        } catch (UnsupportedEncodingException var4) {
                            var4.printStackTrace();
                            throw new SQLException("Data is not UTF-8 encoded, and got an unsupported encoding exception");
                        }
                    }

                    return value;
                case TIMESTAMP_TYPE:
                    return Timestamp.valueOf((String)value);
                case DECIMAL_TYPE:
                    return new BigDecimal((String)value);
                case DATE_TYPE:
                    return HiveDate.valueOf((String)value);
                case INTERVAL_YEAR_TO_MONTH:
                    result = (new BigDecimal(HiveIntervalYearMonth.valueOf((String)value).getTotalMonths())).divide(new BigDecimal(12), 64, 4);
                    return (new BigDecimal(result.toPlainString(), new MathContext(40))).stripTrailingZeros();
                case INTERVAL_DAY_TO_SECOND:
                    result = (new BigDecimal(HiveIntervalDayTime.valueOf((String)value).getTotalSeconds())).divide(new BigDecimal(86400), 64, 4);
                    return (new BigDecimal(result.toPlainString(), new MathContext(40))).stripTrailingZeros();
                case ARRAY_TYPE:
                case MAP_TYPE:
                    return value;
                case STRUCT_TYPE:
                    return new HiveStruct(typeDescriptor.getSubsequentTypes(), (String)value);
                case GEO_TYPE:
                    return value;
                default:
                    return value;
            }
        }
    }

    public Object getObject(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        return this.getColumnValue(columnIndex);
    }

    public Object getObject(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getObject(this.findColumn(columnName));
    }

    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), i);
        throw new SQLException("Method not supported");
    }

    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), colName);
        throw new SQLException("Method not supported");
    }

    public Ref getRef(int i) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), i);
        throw new SQLException("Method not supported");
    }

    public Ref getRef(String colName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), colName);
        throw new SQLException("Method not supported");
    }

    public int getRow() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public RowId getRowId(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public RowId getRowId(String columnLabel) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnLabel);
        throw new SQLException("Method not supported");
    }

    public SQLXML getSQLXML(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public SQLXML getSQLXML(String columnLabel) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnLabel);
        throw new SQLException("Method not supported");
    }

    public short getShort(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);

        try {
            Object obj = this.getObject(columnIndex);
            if (Number.class.isInstance(obj)) {
                return ((Number)obj).shortValue();
            } else if (obj == null) {
                return 0;
            } else if (String.class.isInstance(obj)) {
                return Short.parseShort((String)obj);
            } else {
                throw new Exception("Illegal conversion");
            }
        } catch (Exception var3) {
            throw new SQLException("Cannot convert column " + columnIndex + " to short: " + var3.toString(), var3);
        }
    }

    public short getShort(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getShort(this.findColumn(columnName));
    }

    public Statement getStatement() throws SQLException {
        logger.trace("{}", this.traceInfo());
        return this.statement;
    }

    public String getString(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object value = this.getColumnValue(columnIndex);
        if (columnIndex <= 0) {
            throw new SQLException("Column Index in JDBC starts from 1, not 0");
        } else if (this.wasNull) {
            return null;
        } else if (value instanceof byte[]) {
            try {
                return new String((byte[])((byte[])value), "UTF-8");
            } catch (UnsupportedEncodingException var4) {
                var4.printStackTrace();
                throw new SQLException("Data is not utf-8 encoded, and got an unsupported encoding exception");
            }
        } else {
            return value.toString();
        }
    }

    public String getString(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getString(this.findColumn(columnName));
    }

    public Time getTime(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else if (obj instanceof Time) {
            return (Time)obj;
        } else if (obj instanceof Date) {
            return (Time)obj;
        } else if (obj instanceof HiveTime) {
            return new Time(((HiveTime)obj).getTime());
        } else if (obj instanceof HiveDate) {
            return new Time(((HiveDate)obj).getTime());
        } else if (obj instanceof Timestamp) {
            return new Time(((Timestamp)obj).getTime());
        } else if (obj instanceof String) {
            return this.toTime((String)obj);
        } else {
            throw new SQLException("Illegal conversion");
        }
    }

    public Time getTime(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getTime(this.findColumn(columnName));
    }

    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public Time getTime(String columnName, Calendar cal) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getTime(this.findColumn(columnName), cal);
    }

    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        Object obj = this.getObject(columnIndex);
        if (obj == null) {
            return null;
        } else if (obj instanceof Timestamp) {
            return (Timestamp)obj;
        } else if (obj instanceof Date) {
            return new Timestamp(((Date)obj).getTime());
        } else if (obj instanceof HiveDate) {
            return new Timestamp(((HiveDate)obj).getTime());
        } else if (obj instanceof String) {
            return this.toTimestamp((String)obj);
        } else {
            throw new SQLException("Illegal conversion");
        }
    }

    public Timestamp getTimestamp(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getTimestamp(this.findColumn(columnName));
    }

    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        return this.getTimestamp(this.findColumn(columnName), cal);
    }

    public int getType() throws SQLException {
        logger.trace("{}", this.traceInfo());
        return 1003;
    }

    public URL getURL(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public URL getURL(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        throw new SQLException("Method not supported");
    }

    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnIndex);
        throw new SQLException("Method not supported");
    }

    public InputStream getUnicodeStream(String columnName) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), columnName);
        throw new SQLException("Method not supported");
    }

    public void insertRow() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean isAfterLast() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean isBeforeFirst() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean isClosed() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean isFirst() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean isLast() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean last() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void moveToCurrentRow() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void moveToInsertRow() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean previous() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void refreshRow() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean relative(int rows) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean rowDeleted() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean rowInserted() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean rowUpdated() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void setFetchDirection(int direction) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), direction);
        this.fetchDirection = direction;
    }

    public void setFetchSize(int rows) throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), rows);
        throw new SQLException("Method not supported");
    }

    public void updateArray(int columnIndex, Array x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateArray(String columnName, Array x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateAsciiStream(int columnIndex, InputStream x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateAsciiStream(String columnLabel, InputStream x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateAsciiStream(int columnIndex, InputStream x, int length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateAsciiStream(String columnName, InputStream x, int length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateAsciiStream(int columnIndex, InputStream x, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateAsciiStream(String columnLabel, InputStream x, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBigDecimal(int columnIndex, BigDecimal x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBigDecimal(String columnName, BigDecimal x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBinaryStream(int columnIndex, InputStream x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBinaryStream(String columnLabel, InputStream x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBinaryStream(int columnIndex, InputStream x, int length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBinaryStream(String columnName, InputStream x, int length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBinaryStream(int columnIndex, InputStream x, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBinaryStream(String columnLabel, InputStream x, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBlob(int columnIndex, Blob x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBlob(String columnName, Blob x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBlob(int columnIndex, InputStream inputStream) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBlob(String columnLabel, InputStream inputStream) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBlob(int columnIndex, InputStream inputStream, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBlob(String columnLabel, InputStream inputStream, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBoolean(int columnIndex, boolean x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBoolean(String columnName, boolean x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateByte(int columnIndex, byte x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateByte(String columnName, byte x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBytes(int columnIndex, byte[] x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateBytes(String columnName, byte[] x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateCharacterStream(int columnIndex, Reader x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateCharacterStream(String columnLabel, Reader reader) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateCharacterStream(int columnIndex, Reader x, int length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateClob(int columnIndex, Clob x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateClob(String columnName, Clob x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateClob(int columnIndex, Reader reader) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateClob(String columnLabel, Reader reader) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateClob(int columnIndex, Reader reader, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateClob(String columnLabel, Reader reader, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateDate(int columnIndex, Date x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateDate(String columnName, Date x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateDouble(int columnIndex, double x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateDouble(String columnName, double x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateFloat(int columnIndex, float x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateFloat(String columnName, float x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateInt(int columnIndex, int x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateInt(String columnName, int x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateLong(int columnIndex, long x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateLong(String columnName, long x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNCharacterStream(int columnIndex, Reader x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNCharacterStream(String columnLabel, Reader reader) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNCharacterStream(int columnIndex, Reader x, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNCharacterStream(String columnLabel, Reader reader, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNClob(int columnIndex, NClob clob) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNClob(String columnLabel, NClob clob) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNClob(int columnIndex, Reader reader) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNClob(String columnLabel, Reader reader) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNClob(int columnIndex, Reader reader, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNClob(String columnLabel, Reader reader, long length) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNString(int columnIndex, String string) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNString(String columnLabel, String string) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNull(int columnIndex) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateNull(String columnName) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateObject(int columnIndex, Object x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateObject(String columnName, Object x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateObject(int columnIndex, Object x, int scale) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateObject(String columnName, Object x, int scale) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateRef(int columnIndex, Ref x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateRef(String columnName, Ref x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateRow() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateRowId(int columnIndex, RowId x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateRowId(String columnLabel, RowId x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateSQLXML(int columnIndex, SQLXML xmlObject) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateSQLXML(String columnLabel, SQLXML xmlObject) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateShort(int columnIndex, short x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateShort(String columnName, short x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateString(int columnIndex, String x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateString(String columnName, String x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateTime(int columnIndex, Time x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateTime(String columnName, Time x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateTimestamp(int columnIndex, Timestamp x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public void updateTimestamp(String columnName, Timestamp x) throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public SQLWarning getWarnings() throws SQLException {
        logger.trace("{}", this.traceInfo());
        return this.warningChain;
    }

    public void clearWarnings() throws SQLException {
        logger.trace("{}", this.traceInfo());
        this.warningChain = null;
    }

    public void close() throws SQLException {
        logger.trace("{}", this.traceInfo());
        throw new SQLException("Method not supported");
    }

    public boolean wasNull() throws SQLException {
        logger.trace("{}, {}", this.traceInfo(), this.wasNull);
        return this.wasNull;
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        logger.trace("{}", this.traceInfo());
        boolean f = iface.isInstance(this);
        return f;
    }

    public <T> T unwrap(Class<T> iface) throws SQLException {
        logger.trace("{}", this.traceInfo());

        try {
            T t = iface.cast(this);
            return t;
        } catch (ClassCastException var4) {
            throw new SQLException("Invalid Type cast.", var4);
        }
    }

    protected void setSchema(TableSchema schema) {
        this.schema = schema;
    }

    protected TableSchema getSchema() {
        return this.schema;
    }

    protected void setLastAccessTime() {
        this.lastAccessTime = System.currentTimeMillis();
    }

    protected long getLastAccessTime() {
        return this.lastAccessTime;
    }

    protected Date toDate(String str) throws SQLException {
        str = str.trim();
        String year = "";
        String month = "";
        String day = "";
        int index = 0;

        try {
            String[] parts = str.split("[^\\d]");

            for(int i = 0; i < parts.length; ++i) {
                if (parts[i].length() > 0 && index < 3) {
                    parts[index++] = parts[i];
                }
            }

            if (parts[0].length() == 4) {
                year = parts[0];
                month = parts[1];
                day = parts[2];
            } else if (parts[0].length() == 8) {
                year = parts[0].substring(0, 4);
                month = parts[0].substring(4, 6);
                day = parts[0].substring(6, 8);
            }
        } catch (Exception var8) {
            return Date.valueOf(str);
        }

        return Date.valueOf(year + "-" + month + "-" + day);
    }

    protected Timestamp toTimestamp(String str) throws SQLException {
        str = str.trim();
        String year = "";
        String month = "";
        String day = "";
        String hour = "0";
        String minute = "0";
        String second = "0";
        String ms = "0";
        ArrayList<String> ts = new ArrayList(8);

        try {
            String[] parts = str.split("[^\\d]");

            int index;
            for(index = 0; index < parts.length; ++index) {
                if (parts[index].length() > 0 && ts.size() < 7) {
                    ts.add(parts[index]);
                }
            }

            index = 0;
            if (((String)ts.get(index)).length() == 4) {
                year = (String)ts.get(index++);
                month = (String)ts.get(index++);
                day = (String)ts.get(index++);
            } else if (((String)ts.get(index)).length() == 8) {
                year = ((String)ts.get(index)).substring(0, 4);
                month = ((String)ts.get(index)).substring(4, 6);
                day = ((String)ts.get(index++)).substring(6, 8);
            }

            if (index < ts.size()) {
                hour = parts[index++];
            }

            if (index < ts.size()) {
                minute = parts[index++];
            }

            if (index < ts.size()) {
                second = parts[index++];
            }

            if (index < ts.size()) {
                ms = parts[index++];
            }

            StringBuffer buffer = new StringBuffer();
            buffer.append(year);
            buffer.append("-");
            buffer.append(month);
            buffer.append("-");
            buffer.append(day);
            buffer.append(" ");
            if (hour.length() > 0 && minute.length() > 0 && second.length() > 0) {
                buffer.append(hour);
                buffer.append(":");
                buffer.append(minute);
                buffer.append(":");
                buffer.append(second);
                buffer.append(".");
                buffer.append(ms);
            }

            return Timestamp.valueOf(buffer.toString());
        } catch (Exception var13) {
            return Timestamp.valueOf(str);
        }
    }

    protected Time toTime(String str) throws SQLException {
        str = str.trim();
        String hour = "";
        String minute = "";
        String second = "";
        ArrayList<String> time = new ArrayList(3);

        try {
            String[] parts = str.split("[^\\d]");

            for(int i = 0; i < parts.length; ++i) {
                if (parts[i].length() > 0 && time.size() < 3) {
                    time.add(parts[i]);
                }
            }

            hour = parts[0];
            minute = parts[1];
            second = parts[2];
            return Time.valueOf(hour + ":" + minute + ":" + second);
        } catch (Exception var8) {
            return Time.valueOf(str);
        }
    }

    private String traceInfo() {
        return "@" + Integer.toHexString(this.hashCode());
    }
}