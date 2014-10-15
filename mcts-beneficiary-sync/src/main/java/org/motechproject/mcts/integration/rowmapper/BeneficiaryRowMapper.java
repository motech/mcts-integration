package org.motechproject.mcts.integration.rowmapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import org.motechproject.mcts.integration.model.Beneficiary;
import org.springframework.jdbc.core.RowMapper;

public class BeneficiaryRowMapper implements RowMapper<Beneficiary> {

    @Override
    public Beneficiary mapRow(ResultSet resultSet, int index) throws SQLException {

        Beneficiary beneficiary = new Beneficiary(
                (Integer) resultSet.getInt("mother_id"), resultSet.getString("mcts_id"),
                (Integer) resultSet.getInt("service_type"),
                (Date) resultSet.getDate("delivery_date"), resultSet.getString("mobile_number"),
                (Integer) resultSet.getInt("anc1_hblevel"), (Integer) resultSet.getInt("anc2_hblevel"),
                (Integer) resultSet.getInt("anc3_hblevel"), (Integer) resultSet.getInt("anc4_hblevel"));
        return beneficiary;
    }
}