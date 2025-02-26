package com.sjzu.edu.index;

import com.jfinal.plugin.activerecord.Db;
import com.jfinal.plugin.activerecord.Page;
import com.jfinal.plugin.activerecord.Record;
import com.sjzu.edu.common.model.Drivergpsinfo;

import java.util.List;

/** 2024-3-10 jason
 */
public class CaryunxingService {

	/**  此操作对应的表：drivergpsinfo
	 *
	 */
	private Drivergpsinfo dao = new Drivergpsinfo().dao();

	public Page<Drivergpsinfo> paginate(int pageNumber, int pageSize) {
		return dao.paginate(pageNumber, pageSize, "select *", "from drivergpsinfo order by id desc");
	}

	public Page<Drivergpsinfo> search(int pageNumber, int pageSize, String carno) {
		String addSql = "";

		// 构建动态查询条件
		if (carno != null && !carno.isEmpty()) {
			addSql = "WHERE carno LIKE '%" + carno + "%' ";
		}

		String sql = "FROM drivergpsinfo " + addSql + "ORDER BY id DESC";

		return dao.paginate(pageNumber, pageSize, "SELECT *", sql);
	}

	public Drivergpsinfo findById(int id) {
		return dao.findById(id);
	}

	// 新增方法：根据车牌号和时间戳查询过去24小时的经纬度
	public List<Drivergpsinfo> findLocationsByCarnoAndTime(String carno, String timestamp) {
		String sql = "SELECT jingdu, weidu FROM drivergpsinfo " +
				"WHERE carno = ? AND updatetime >= DATE_SUB(?, INTERVAL 24 HOUR) " +
				"ORDER BY updatetime DESC";
		return dao.find(sql, carno, timestamp);
	}
}
