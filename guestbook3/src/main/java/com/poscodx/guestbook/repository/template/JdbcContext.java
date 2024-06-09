package com.poscodx.guestbook.repository.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceUtils;

public class JdbcContext {
	private DataSource dataSource;
	
	public JdbcContext(DataSource dataSource) {
		this.dataSource = dataSource;
	}
	
	public <E> List<E> queryForList(String sql, RowMapper<E> rowMapper) {
		return executeQueryForListWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				return pstmt;
			}
		}, rowMapper);
	}

	public <E> List<E> queryForList(String sql, Object[] parameters, RowMapper<E> rowMapper) {
		return executeQueryForListWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				return pstmt;
			}
		}, rowMapper);
	}

	public <T> T queryForObject(String sql, RowMapper<T> rowMapper) {
		return executeQueryForObjectWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);			
				return pstmt;
			}
		}, rowMapper);
	}

	public <T> T queryForObject(String sql, Object[] parameters, RowMapper<T> rowMapper) {
		return executeQueryForObjectWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				for(int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i+1, parameters[i]);
				}				
				return pstmt;
			}
		}, rowMapper);
	}

	public int update(String sql) {
		return executeUpdateWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				return pstmt;
			}
		});
	}
	
	public int update(String sql, Object[] parameters) {
		return executeUpdateWithStatementStrategy(new StatementStrategy() {
			@Override
			public PreparedStatement makeStatement(Connection connection) throws SQLException {
				PreparedStatement pstmt = connection.prepareStatement(sql);
				for(int i = 0; i < parameters.length; i++) {
					pstmt.setObject(i+1, parameters[i]);
				}
				return pstmt;
			}
		});
	}
	
	private <T> T executeQueryForObjectWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<T> rowMapper) {
		T result = null;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			
			pstmt = statementStrategy.makeStatement(conn);
			rs = pstmt.executeQuery();
			
			if(rs.next()) {
				result = rowMapper.mapRow(rs, rs.getRow());
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.toString());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					DataSourceUtils.releaseConnection(conn, dataSource);
				}
			} catch(SQLException ignored) {
			}
		}
		
		return result;
	}
	
	private <E> List<E> executeQueryForListWithStatementStrategy(StatementStrategy statementStrategy, RowMapper<E> rowMapper) {
		List<E> result = new ArrayList<>();
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			
			pstmt = statementStrategy.makeStatement(conn);
			rs = pstmt.executeQuery();
			
			while(rs.next()) {
				E e = rowMapper.mapRow(rs, rs.getRow());
				result.add(e);
			}
		} catch (SQLException e) {
			throw new RuntimeException(e.toString());
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					DataSourceUtils.releaseConnection(conn, dataSource);
				}
			} catch(SQLException ignored) {
			}
		}
		
		return result;
	}

	private int executeUpdateWithStatementStrategy(StatementStrategy statementStrategy) {
		int result = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		
		try {
			conn = DataSourceUtils.getConnection(dataSource);
			
			pstmt = statementStrategy.makeStatement(conn);
			result = pstmt.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e.toString());
		} finally {
			try {
				if(pstmt != null) {
					pstmt.close();
				}
				if(conn != null) {
					DataSourceUtils.releaseConnection(conn, dataSource);
				}
			} catch(SQLException ignored) {
			}
		}
		
		return result;		
	}
}
