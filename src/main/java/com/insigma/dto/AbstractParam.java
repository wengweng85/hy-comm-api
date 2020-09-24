package com.insigma.dto;

import java.io.Serializable;

public abstract class AbstractParam
implements Serializable
{
protected String userId;
protected String loginId;

public String getUserId()
{
  return this.userId;
}

public void setUserId(String userId)
{
  this.userId = userId;
}

public String getLoginId()
{
  return this.loginId;
}

public void setLoginId(String loginId)
{
  this.loginId = loginId;
}
}

