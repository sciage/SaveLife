package com.donate.savelife.core.requirement.service;

import com.donate.savelife.core.database.DatabaseResult;
import com.donate.savelife.core.requirement.model.Need;
import com.donate.savelife.core.requirement.model.Needs;
import com.donate.savelife.core.user.data.model.User;
import com.donate.savelife.core.user.data.model.Users;

import rx.Observable;

public interface NeedService {

    Observable<DatabaseResult<Needs>> observeNeedsWithUsers(User user);

    Observable<DatabaseResult<Needs>> observeNeeds(User user);

    Observable<DatabaseResult<Needs>> observeMoreNeeds(User user, Need need);

    Observable<DatabaseResult<Needs>> observeMoreNeedsWithUsers(User user, Need need);

    Observable<DatabaseResult<Need>> writeNeed(Need need);

    Observable<DatabaseResult<Users>> observeUserIdsFor(User user);

    Observable<DatabaseResult<Users>> observeMoreUserIdsFor(User user, Need need);

    Observable<DatabaseResult<Need>> observeNeed(String needID);

}
