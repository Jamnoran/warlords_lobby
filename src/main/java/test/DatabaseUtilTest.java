package test;


class DatabaseUtilTest {

//	@Test
//	public void testGenerateLoginKey(){
//		System.out.println("Generated key is : " + DatabaseUtil.generateLoginKey());
//	}
//
//	@Test
//	public void testSignCalculation(){
//		Gson gson = new Gson();
//		String reqJson = "{\"email\":\"lindahl.eric@gmail.com\",\"password\":\"gameTests\",\"request_type\":\"LOGIN_USER\",\"user_id\":null,\"sign\":\"56EFD0189020ACD9194E34B046A0F169750AE89CD2A08AEDC6CC666397984C66\"}";
//		LoginUserRequest createUserRequest = gson.fromJson(reqJson, LoginUserRequest.class);
//		createUserRequest.setSign(null);
//		// TODO: Fix that it now requires user etc
////		assert (AuthenticationService.checkAuthentication(createUserRequest));
//	}
//
//	@Test
//	public void testGetUser(){
//		User user = DatabaseUtil.getUser("lindahl.eric@gmail.com", "test");
//		assert(user != null);
//		assert (user.getLoginKey() != null);
//		Log.i(DatabaseUtilTest.class.getSimpleName(), "Got loginKey: " + user.getLoginKey());
//	}
//
//	@Test
//	public void testGetUsername(){
//		User user = DatabaseUtil.getUser(6);
//		String userName = "";
//		if(user != null && user.getUsername() != null){
//			userName = user.getUsername();
//		}
//		String data = new Gson().toJson(new UsernameResponse(userName));
//		System.out.println("Response  : " + data);
//
//	}
}