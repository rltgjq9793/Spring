package org.zerock.security;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.zerock.domain.MemberVO;
import org.zerock.mapper.MemberMapper;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration({"file:src/main/webapp/WEB-INF/spring/root-context.xml",
						"file:src/main/webapp/WEB-INF/spring/security-context.xml"})
@Log4j
public class MemberTests {

	@Setter(onMethod_=@Autowired)
	private PasswordEncoder pwencoder;
	
	@Setter(onMethod_=@Autowired)
	private DataSource ds;

	@Setter(onMethod_=@Autowired)
	private MemberMapper mapper;
	
	@Test
	public void test1() {
		
		log.info(pwencoder);
		
		String enpw = pwencoder.encode("member");
		
		log.info(enpw);
		
		boolean result = pwencoder.matches("member", enpw);
		
	}
	
	@Test
	public void testInsertMember() {
		
		String sql = "insert into tbl_member(userid,userpw,username) values (?,?,?)";

		
		for(int i=0; i<100; i++) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, "user" + i);
				pstmt.setString(2, pwencoder.encode("pw"+i));
				pstmt.setString(3, "사용자" + i);
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
			}finally {
				if(pstmt !=null) { try{ pstmt.close();}catch(Exception e) {} };
				if(con !=null) { try{ con.close();}catch(Exception e) {} };
			}
		}
		
		
	}
	
	@Test
	public void testInsertAuth() {
		String sql = "insert into tbl_member_auth(userid, auth) values(?,?)";
		
		for(int i=90; i<100; i++) {
			
			Connection con = null;
			PreparedStatement pstmt = null;
			
			try {
				con = ds.getConnection();
				pstmt = con.prepareStatement(sql);
				
				pstmt.setString(1, "user" + i);
			
				pstmt.setString(2, "ROLE_ADMIN");
				
				
				pstmt.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
				
			}finally {
				if(pstmt !=null) { try{ pstmt.close();}catch(Exception e) {} };
				if(con !=null) { try{ con.close();}catch(Exception e) {} };
			}
		}
		
	}

	@Test
	public void mapperTest() {
		log.info(mapper.getTime());
	}

	@Test
	public void getMember() {
		MemberVO vo = mapper.getMember("user92");
		log.info(vo);
	}

}
