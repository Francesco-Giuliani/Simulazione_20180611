package it.polito.tdp.ufo.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Year;
import java.time.temporal.TemporalField;
import java.util.ArrayList;
import java.util.List;

import it.polito.tdp.ufo.model.Sighting;
import it.polito.tdp.ufo.model.StateBeforeState;
import it.polito.tdp.ufo.model.YearAndCount;

public class SightingsDAO {
	
	public List<Sighting> getSightings() {
		String sql = "SELECT * FROM sighting" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Sighting> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				list.add(new Sighting(res.getInt("id"),
						res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), 
						res.getString("state"), 
						res.getString("country"),
						res.getString("shape"),
						res.getInt("duration"),
						res.getString("duration_hm"),
						res.getString("comments"),
						res.getDate("date_posted").toLocalDate(),
						res.getDouble("latitude"), 
						res.getDouble("longitude"))) ;
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null ;
		}
	}

	public List<YearAndCount> getAnniNumAvvistamenti() {
		final String sql = "select YEAR(datetime) as anno, count(*) num\r\n" + 
				"from sighting\r\n" + 
				"where country = 'us'\r\n" + 
				"group by anno\r\n" + 
				"order by anno";
		List<YearAndCount> anni = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				YearAndCount y = new YearAndCount(Year.of(rs.getInt("anno")), rs.getInt("num"));
				anni.add(y);
			}
			st.close();
			conn.close();
			return anni;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore DB");
		}
	}

	public List<String> getStati(Year anno) {
		String sql = "select DISTINCT state\n" + 
				"from sighting\n" + 
				"where country = 'us' and YEAR(datetime) = ? \n"+
				"order by state";
		List<String> stati = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				String y = rs.getString("state");
				stati.add(y);
			}
			st.close();
			conn.close();
			return stati;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore DB");
		}
	}

	public List<StateBeforeState> getAlledges(Year anno){
		String sql = "select s1.state st1, s2.state st2\r\n" + 
				"from sighting s1, sighting s2\r\n" + 
				"where YEAR(s1.datetime) = YEAR(s2.datetime) and YEAR(s1.datetime) = ?\r\n" + 
				"and s1.country = 'us' and s2.country = 'us'\r\n" + 
				"and s1.state is not null and s2.state is not null\r\n" + 
				"and s1.state != s2.state\r\n" + 
				"and s2.datetime>s1.datetime\r\n" + 
				"group by st1, st2";
		List<StateBeforeState> edges = new ArrayList<>();
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				StateBeforeState y = new StateBeforeState(rs.getString("st1"), rs.getString("st2"));
				edges.add(y);
			}
			st.close();
			conn.close();
			return edges;
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore DB");
		}
	}
	
	public boolean esisteAvvistamento(Year anno, String st1, String st2) {
		String sql = "select count(*) as num\r\n" + 
				"from sighting s1, sighting s2\r\n" + 
				"where YEAR(s1.datetime) = YEAR(s2.datetime) and YEAR(s1.datetime) = ?\r\n" + 
				"and s1.country = 'us' and s2.country='us'  \r\n" + 
				"and s1.state = ?\r\n" + 
				"and s2.state = ?\r\n" + 
				"and s2.datetime>s1.datetime";
		/*METODO IN CUI PRENDO TUTTI GLI ARCHI DIRETTAMNTE CON UNA QUERY SQL
		select s1.state st1, s2.state st2, count(*)
from sighting s1, sighting s2
where YEAR(s1.datetime) = YEAR(s2.datetime) and YEAR(s1.datetime) = 2000
and s1.state = 'ca'
and s2.state = 'ny'
and s2.datetime>s1.datetime
group by st1, st2
		*/
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno.getValue());
			st.setString(2,  st1);
			st.setString(3, st2);
			ResultSet rs = st.executeQuery();
			
			if(rs.next())
				if(rs.getInt("num")>0) {
					st.close();
					conn.close();
					return true;
					}
				else {
					st.close();
					conn.close();
					return false;
				}
			else {
				st.close();
				conn.close();
				return false;
			}
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore DB");
		}
	}

	
}
