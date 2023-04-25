package com.kodeklubben.boardwave.repositories;
import com.kodeklubben.boardwave.services.DatabaseConnectionManager;

@org.springframework.stereotype.Repository
public class Repository {
    private DatabaseConnectionManager dcm = new DatabaseConnectionManager("eu-west.connect.psdb.cloud", "n9yvymfj507ekkjlhtkd", "pscale_pw_iJs7WNJ0mxHfCdADkqSRM1GGcSUK5GgNiKGcgzgSz5m");

}
