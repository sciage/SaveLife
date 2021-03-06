package com.donate.savelife.core.user.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.database.NeedDatabase;
import com.donate.savelife.core.user.data.model.Heros;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.database.HeroDatabase;
import com.donate.savelife.core.user.database.UserDatabase;

import rx.Observable;
import rx.functions.Func1;

/**
 * Created by ravi on 07/12/16.
 */

public class PersistedHeroService implements HeroService {

    private final HeroDatabase heroDatabase;
    private final UserDatabase userDatabase;
    private final NeedDatabase needDatabase;

    public PersistedHeroService(HeroDatabase heroDatabase, UserDatabase userDatabase, NeedDatabase needDatabase) {
        this.heroDatabase = heroDatabase;
        this.userDatabase = userDatabase;
        this.needDatabase = needDatabase;
    }

    @Override
    public Observable<DatabaseResult<Heros>> observerHeros(String needID) {
        return heroDatabase.observeHeros(needID)
                .map(asHerosDatabaseResult())
                .onErrorReturn(DatabaseResult.<Heros>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<User>> observeHero(String needID, String userID) {
        return heroDatabase.observeHeroFrom(needID, userID)
                .map(asHeroDatabaseResult())
                .filter(isHeroAddedSuccessfully())
                .flatMap(getUser())
                .onErrorReturn(DatabaseResult.<User>errorAsDatabaseResult());
    }

    @Override
    public Observable<DatabaseResult<User>> saveHero(String needId, String userID) {
        return heroDatabase.saveHero(needId, userID)
                .map(asHeroDatabaseResult())
                .filter(isHeroAddedSuccessfully())
                .flatMap(getUser())
                .filter(isUserFetchSuccessfully())
                .flatMap(updateCount())
                .onErrorReturn(DatabaseResult.<User>errorAsDatabaseResult());
    }

    private Func1<Heros, DatabaseResult<Heros>> asHerosDatabaseResult() {
        return new Func1<Heros, DatabaseResult<Heros>>() {
            @Override
            public DatabaseResult<Heros> call(Heros heros) {
                return new DatabaseResult<Heros>(heros);
            }
        };
    }

    private Func1<String, DatabaseResult<String>> asHeroDatabaseResult() {
        return new Func1<String, DatabaseResult<String>>() {
            @Override
            public DatabaseResult<String> call(String userID) {
                return new DatabaseResult<String>(userID);
            }
        };
    }


    private Func1<DatabaseResult<String>, Boolean> isHeroAddedSuccessfully(){
        return new Func1<DatabaseResult<String>, Boolean>() {
            @Override
            public Boolean call(DatabaseResult<String> stringDatabaseResult) {
                return stringDatabaseResult.isSuccess() ? true : false;
            }
        };
    }

    private Func1<DatabaseResult<User>, Boolean> isUserFetchSuccessfully(){
        return new Func1<DatabaseResult<User>, Boolean>() {
            @Override
            public Boolean call(DatabaseResult<User> userDatabaseResult) {
                return userDatabaseResult.isSuccess() ? true : false;
            }
        };
    }

    private Func1<DatabaseResult<String>, Observable<DatabaseResult<User>>> getUser(){
        return new Func1<DatabaseResult<String>, Observable<DatabaseResult<User>>>() {
            @Override
            public Observable<DatabaseResult<User>> call(DatabaseResult<String> stringDatabaseResult) {
                return userDatabase.readUserFrom(stringDatabaseResult.getData())
                        .map(asUserDatabaseResult());
            }
        };
    }



    private Func1<User, DatabaseResult<User>> asUserDatabaseResult() {
        return new Func1<User, DatabaseResult<User>>() {
            @Override
            public DatabaseResult<User> call(User user) {
                return new DatabaseResult<User>(user);
            }
        };
    }

    private  Func1<DatabaseResult<User>, Observable<DatabaseResult<User>>> updateCount(){
        return new Func1<DatabaseResult<User>,Observable<DatabaseResult<User>>>() {
            @Override
            public Observable<DatabaseResult<User>> call(DatabaseResult<User> userDatabaseResult) {
                return userDatabase.updateTheLifeCount(userDatabaseResult.getData())
                        .map(asUserDatabaseResult());
            }
        };
    }

    private Func1<DatabaseResult<Heros>, Boolean> isHeroHonored(){
        return new Func1<DatabaseResult<Heros>, Boolean>() {
            @Override
            public Boolean call(DatabaseResult<Heros> herosDatabaseResult) {
                return herosDatabaseResult.isSuccess() ? true : false;
            }
        };
    }

}
