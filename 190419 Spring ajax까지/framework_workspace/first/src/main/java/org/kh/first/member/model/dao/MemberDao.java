package org.kh.first.member.model.dao;

import org.kh.first.member.model.vo.Member;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

@Repository("memberDao")
public class MemberDao {

	public Member selectLogin(SqlSessionTemplate mybatisSession, Member member) {
		return mybatisSession.selectOne("memberMapper.selectLogin", member);
	}

	public int insertMember(SqlSessionTemplate mybatisSession, Member member) {
		return mybatisSession.insert("memberMapper.insertMember", member);
	}

	public Member selectMember(SqlSessionTemplate mybatisSession, String userid) {
		return mybatisSession.selectOne("memberMapper.selectMember", userid);
	}

	public int deleteMember(SqlSessionTemplate mybatisSession, String userid) {
		return mybatisSession.delete("memberMapper.deleteMember", userid);
	}

	public int updateMember(SqlSessionTemplate mybatisSession, Member member) {
		return mybatisSession.update("memberMapper.updateMember", member);
	}

}
