package com.kcj.peninkframe.ui;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.kcj.peninkframe.bean.Address;
import com.kcj.peninkframe.bean.Boss;
import com.kcj.peninkframe.bean.Company;
import com.kcj.peninkframe.bean.Man;
import com.kcj.peninkframe.bean.Wife;
import com.kcj.peninkframe.utils.Log;
import com.kcj.peninkframe.utils.Toastor;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.DataBase;
import com.litesuits.orm.db.assit.QueryBuilder;
import com.litesuits.orm.db.assit.WhereBuilder;
import com.litesuits.orm.db.model.ColumnsValue;
import com.litesuits.orm.db.model.ConflictAlgorithm;


/**
 * @ClassName: SampleOrmActivity
 * @Description: 数据库
 * @author: KCJ
 * @date:
 */
public class SampleOrmActivity extends Activity implements OnItemClickListener{

	DataBase db;
	private ListView listView;
	
	static Man uComplex, uAlice, uMax, uMin;
    /**
     * object relation mapping test
     * man:address -> 1:n
     */
    static ConcurrentLinkedQueue<Address> addrList;
    /**
     * man:teacher -> n:n
     */
    static ArrayList<Boss> bossList;
    /**
     * man:company -> n:1
     */
    static Company company;
    /**
     * man:wife -> 1:1
     */
    static Wife wife1, wife2;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		db = LiteOrm.newSingleInstance(this, "liteorm.db");
		mockData();
		listView = new ListView(this);
		listView.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_expandable_list_item_1, getData()));
		setContentView(listView);
		listView.setOnItemClickListener(this);
	}
	
	private List<String> getData(){
        List<String> data = new ArrayList<String>();
        data.add("Save(Insert Or Update)");
        data.add("Insert");
        data.add("Update");
        data.add("Update Column");
        data.add("Query All");
        data.add("Query By WhereBuilder");
        data.add("Query By ID");
        data.add("Query Any U Want");
        data.add("Relation Restore");
        data.add("Delete");
        data.add("Delete By Index");
        data.add("Delete By WhereBuilder");
        data.add("Delete All");
        return data;
    }
	
	private void mockData() {
        if (uAlice != null) {
            return;
        }
        uAlice = new Man(0, "alice", 18, false, (short) 12345, (byte) 123, 0.56f, 123.456d, 'c');
        uMax = new Man(0, "max", 99, false, Short.MAX_VALUE, Byte.MAX_VALUE, Float.MAX_VALUE, Double.MAX_VALUE,
                       Character.MAX_VALUE);
        uMin = new Man(0, "min", 1, true, Short.MIN_VALUE, Byte.MIN_VALUE, Float.MIN_VALUE, Double.MIN_VALUE,
                       Character.MIN_VALUE);
        uComplex = new Man(0, null, 0, false);
        uComplex.name = "complex";
        uComplex.setAge(18);
        uComplex.aShort = 32766;
        uComplex.aByte = 126;
        uComplex.aFloat = Float.MAX_VALUE;
        uComplex.setaDouble(Double.MAX_VALUE);
        uComplex.setLogin(true);
        uComplex.setDate(new Date(System.currentTimeMillis()));
        uComplex.setImg(new byte[]{23, 34, 77, 23, 19, 11});
        uComplex.def_bool = true;
        uComplex.def_int = 922;
        uComplex.conflict = "cutom";

        uComplex.map = new HashMap<Long, String>();
        uComplex.map.put(1002L, "1002 sdfsd324443534534534");
        uComplex.map.put(1003L, "1003 3dfgdfg24443534534534");
        uComplex.map.put(1004L, "1004 sdfsdg324443534534534");
        uComplex.map.put(1005L, "1005 dfsfd324443534534534");
        // 1 to N
        addrList = new ConcurrentLinkedQueue<Address>();
        addrList.add(new Address("1 西湖  ", "杭州"));
        addrList.add(new Address("2 武林  ", "杭州"));
        addrList.add(new Address("3 西二旗", "北京"));
        addrList.add(new Address("4 公主坟", "北京"));
        addrList.add(new Address("夫子庙", "南京"));
        addrList.add(new Address("中山陵", "南京"));
        addrList.add(new Address("西山陵", "南京"));
        addrList.add(new Address("香港路", "南京"));
        addrList.add(new Address("香港路", "杭州"));
        addrList.add(new Address("香港路", "青岛"));
        addrList.add(new Address("海尔路", "青岛"));
        addrList.add(new Address("海信路", "青岛"));
        uMax.addrList = addrList;

        // N to N
        ArrayList<Man> manlist = new ArrayList<Man>();
        manlist.add(uAlice);
        manlist.add(uComplex);

        bossList = new ArrayList<Boss>();
        Boss cang = new Boss("Cang boss", manlist);
        Boss song = new Boss("Song boss", manlist);
        bossList.add(cang);
        bossList.add(song);

        uAlice.bosses = bossList;
        uComplex.bosses = bossList;

        // 1 To 1
        wife1 = new Wife("Echo", uComplex);
        wife1.type = Wife.Type.enumOne;
        uComplex.wife = wife1;
        wife2 = new Wife("Yamaidi", uMax);
        wife2.type = Wife.Type.enumTwo;
        uMax.wife = wife2;

        // N To 1
        company = new Company("Apple Tech Co.Ltd", manlist);
        uComplex.company = company;
        uAlice.company = company;

        // Array
        //		uComplex.addrArray = new Address[2];
        //		uComplex.addrArray[0] = new Address("Array 0 Xihu Hangzhou China", uComplex);
        //		uComplex.addrArray[1] = new Address("Array 1 Xihu Hangzhou China", uComplex);

        //stack
        //		uComplex.addrIds = new Stack<Long>();
        //		uComplex.addrArray[0] = new Address("Array 0 Xihu Hangzhou China", uComplex);
        //		uComplex.addrArray[1] = new Address("Array 1 Xihu Hangzhou China", uComplex);

        System.out.println(uComplex);
        System.out.println(uAlice);
        System.out.println(uMax);
        System.out.println(uMin);
    }
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		switch (arg2) {
		case 0: // Save(Insert Or Update)
			testSave();
			break;
		case 1: // Insert
			testInsert();
			break;
		case 2: // Update
			testUpdate();
			break;
		case 3: // Update Column
			testUpdateColumn();
			break;
		case 4: // Query All
			testQueryAll();
			break;
		case 5: // Query By WhereBuilder
			testQueryByWhere();
			break;
		case 6: // Query By ID
			testQueryByID();
			break;
		case 7: // Query Any U Want
			testQueryAnyUwant();
			break;
		case 8: // Relation Restore
			testMapping();
			break;
		case 9: // Delete
			testDelete();
			break;
		case 10: // Delete By Index
			testDeleteByIndex();
			break;
		case 11: // Delete By WhereBuilder
			testDeleteByWhereBuilder();
			break;
		case 12: // Delete All
			testDeleteAll();
			break;
		}
	}
	
	private void testSave() {
        db.save(uMax);
        db.save(uMin);
        db.save(company);
        db.save(wife1);
        db.save(wife2);
        //插入任意集合
        db.save(addrList);
        //保存任意集合
        db.save(bossList);
    }
	
	private void testInsert() {
        db.insert(uAlice, ConflictAlgorithm.Replace);
        db.insert(uComplex, ConflictAlgorithm.Rollback);
    }
	
	private void testUpdate() {

        //交换2个User的信息
        long id = uMax.getId();
        uMax.setId(uMin.getId());
        uMin.setId(id);

        // save : 既可以当insert 也可以做update，非常灵活
        long c = db.save(uMax);
        Log.i(this, "update User Max: " + c);

        // update：仅能在已经存在时更新
        c = db.update(uMin, ConflictAlgorithm.Replace);
        Log.i(this, "update User Min: " + c);

        //更新任意的整个集合
        bossList.get(0).setName("Cang Jin Kong");
        bossList.get(1).setName("Song Dao Feng");
        db.update(bossList, ConflictAlgorithm.Fail);
    }
	
	/**
     * 仅更新指定字段
     */
    private void testUpdateColumn() {

        //1. 集合更新实例：
        Boss t1 = bossList.get(0);
        t1.address = "随意写个乱七八糟的地址，反正我不会更新它";
        // 仅更新这一个字段
        t1.phone = "168 8888 8888";
        Boss t2 = bossList.get(1);
        t2.address = "呵呵呵呵呵";
        t2.phone = "168 0000 0000";

        ColumnsValue cv = new ColumnsValue(new String[]{"phone"});
        long c = db.update(bossList, cv, ConflictAlgorithm.None);
        Log.i(this, "update teacher ：" + c);


        //2. 更新单个实体（强制赋指定值）示例：
        wife1.des = "随意写个乱七八糟的描述，反正它会被覆盖";
        wife1.bm = "实体自带值";
        wife1.age = 18;
        cv = new ColumnsValue(new String[]{"des", "bm", "age"}, new Object[]{"外部强制赋值地址", null, 20});
        c = db.update(wife1, cv, ConflictAlgorithm.None);
        Log.i(this, "update wife1 " + wife1.name + ": " + c);
    }
    
    private void testQueryAll() {
        ArrayList<Man> query = db.query(Man.class);
        ArrayList<Address> as = db.query(Address.class);
        ArrayList<Wife> ws = db.query(Wife.class);
        ArrayList<Company> cs = db.query(Company.class);
        ArrayList<Boss> ts = db.query(Boss.class);
        for (Address uu : as) {
            Log.i(this, "query Address: " + uu);
        }
        for (Wife uu : ws) {
            Log.i(this, "query Wife: " + uu);
        }
        for (Company uu : cs) {
            Log.i(this, "query Company: " + uu);
        }
        for (Boss uu : ts) {
            Log.i(this, "query Teacher: " + uu);
        }
        for (Man uu : query) {
            Log.i(this, "query user: " + uu);
        }
    }
	
    private void testQueryByWhere() {
        // 模糊查询：所有带“山”字的地址
        QueryBuilder qb = new QueryBuilder(Address.class)
                .where(Address.COL_ADDRESS + " LIKE ?", new String[]{"%山%"});
        printAddress(db.<Address>query(qb));

        //AND关系 获取 南京的香港路
        qb = new QueryBuilder(Address.class)
        .whereEquals(Address.COL_CITY, "南京")
        .whereAppendAnd()
        .whereEquals(Address.COL_ADDRESS, "香港路");
        printAddress(db.<Address>query(qb));

        //OR关系 获取所有 地址为香港路 ，和 青岛 的所有地址
        qb = new QueryBuilder(Address.class)
        .whereEquals(Address.COL_ADDRESS, "香港路")
        .whereAppendOr()
        .whereEquals(Address.COL_CITY, "青岛");
        printAddress(db.<Address>query(qb));

      //IN语句 获取所有 城市为杭州 和 北京 的地址
        qb = new QueryBuilder(Address.class)
                .whereIn(Address.COL_CITY, new String[]{"杭州", "北京"});
        printAddress(db.<Address>query(qb));

        //IN语句 获取所有 非香港路 并且 ID>10
        qb = new QueryBuilder(Address.class)
                .whereNoEquals(Address.COL_ADDRESS, "香港路")
                .whereAppendAnd()
                .whereGreaterThan(Address.COL_ID, 5);
        printAddress(db.<Address>query(qb));
    }
    
    private void printAddress(List<Address> addrList) {
        for (Address uu : addrList) {
            Log.i(this, "Address: " + uu);
        }
    }
    
    private void testQueryByID() {
        Man man = db.queryById(uComplex.getId(), Man.class);
        Log.i(this, "query id: " + uComplex.getId() + ",MAN: " + man);
    }
    
    private void testQueryAnyUwant() {

        long nums = db.queryCount(Address.class);
        Log.i(this, "Address All Count : " + nums);

        QueryBuilder qb = new QueryBuilder(Address.class)
                .columns(new String[]{Address.COL_ADDRESS})
                .appendOrderAscBy(Address.COL_ADDRESS)
                .appendOrderDescBy(Address.COL_ID)
                .distinct(true)
                .where(Address.COL_ADDRESS + "=?", new String[]{"香港路"});

        nums = db.queryCount(qb);
        Log.i(this, "Address All Count : " + nums);
        List<Address> addrList = db.query(qb);
        for (Address uu : addrList) {
            Log.i(this, "Query Address: " + uu);
        }

    }
    
    private void testMapping() {
        // 先找出来相关的实体
        ArrayList<Man> mans = db.query(Man.class);
        ArrayList<Address> as = db.query(Address.class);
        ArrayList<Wife> ws = db.query(Wife.class);
        ArrayList<Company> cs = db.query(Company.class);
        ArrayList<Boss> ts = db.query(Boss.class);
        // 为它们映射关系
        db.mapping(mans, as);
        db.mapping(mans, ws);
        db.mapping(mans, cs);
        db.mapping(mans, ts);
        for (Address uu : as) {
            Log.i(this, "query Address: " + uu);
        }
        for (Wife uu : ws) {
            Log.i(this, "query Wife: " + uu);
        }
        for (Company uu : cs) {
            Log.i(this, "query Company: " + uu);
        }
        for (Boss uu : ts) {
            Log.i(this, "query Teacher: " + uu);
        }
        //可以看到与Man关联的Teacher、Company、Address都智能映射给Man对应的各个的实例了。
        for (Man uu : mans) {
            Log.i(this, "query user: " + uu);
        }
    }
    
    private void testDelete() {
        db.delete(uMin);
        db.delete(uMax);
        db.delete(uAlice);
        db.delete(uComplex);

        // delete 任意 collection
        db.delete(bossList);

    }

    private void testDeleteByIndex() {
        // 最后一个参数可为null，默认按ID升序排列
        // 按id升序，删除[2, size-1]，结果：仅保留第一个和最后一个
        db.delete(Address.class, 2, addrList.size() - 1, Address.COL_ID);
    }
    
    private void testDeleteByWhereBuilder() {
    	//AND关系 删掉 南京 的 香港路
    	db.delete(WhereBuilder.create(Address.class)
                                   .equals(Address.COL_ADDRESS, "香港路")
                                   .andEquals(Address.COL_CITY, "南京"));
        printAllAddress();

        //OR关系 删掉所有地址为 香港路 ，同时删掉 青岛的所有地址
        db.delete(WhereBuilder.create(Address.class)
                                   .equals(Address.COL_ADDRESS, "香港路")
                                   .orEquals(Address.COL_CITY, "青岛"));
        printAllAddress();

        //IN语句 删掉所有城市为 杭州 或 北京的地址
        db.delete(WhereBuilder.create(Address.class)
                                   .in(Address.COL_CITY, new String[]{"杭州", "北京"}));
        printAllAddress();

        //IN语句 删掉所有 非香港路 并且 ID>10
        db.delete(WhereBuilder.create(Address.class)
                                   .equals(Address.COL_ADDRESS, "夫子庙")
                                   .and()
                                   .greaterThan(Address.COL_ID, 5));
        printAllAddress();
    }
    
    private void printAllAddress() {
        printAddress(db.query(Address.class));
    }

    private void testDeleteAll() {
        db.deleteAll(Address.class);
        db.deleteAll(Company.class);
        db.deleteAll(Wife.class);
        db.deleteAll(Man.class);
        db.deleteAll(Boss.class);
    }
    
    public void ShowToast(final String text) {
		if (!TextUtils.isEmpty(text)) {
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toastor.ShowToast(text);
				}
			});
		}
	}
}
