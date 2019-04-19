package org.kh.first.test.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.kh.first.test.model.vo.Sample;
import org.kh.first.test.model.vo.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TestController {
	//testAjaxPage.jsp 파일 내보내기용
	@RequestMapping("moveAjax.do")
	public String moveAjaxPage() {
		return "test/testAjaxPage";
	}
	
	//ajax test method ------------------------------------------
	@RequestMapping(value="test1.do", method=RequestMethod.POST)
	public void test1Method(Sample sample, HttpServletResponse response) throws IOException {
		System.out.println("sample : " + sample);
		response.setContentType("text/html; charset=utf-8");
		PrintWriter out = response.getWriter();
		if(sample.getName().equals("박혁거세")) {
			out.append("ok");
			out.flush();
		}else {
			out.append("fail");
			out.flush();
		}
		out.close();
		
	}
	
	@RequestMapping(value="test2.do", method=RequestMethod.POST)
	@ResponseBody  //리턴하는 json 문자열을 response 객체에 담아서 보내라는 의미의 어노테이션임
	public String test2Method(HttpServletResponse response) throws UnsupportedEncodingException {
		response.setContentType("application/json; charset=utf-8");
		
		JSONObject job = new JSONObject();
		job.put("no", 127);
		job.put("title", URLEncoder.encode("ajax로 json 리턴 테스트", "UTF-8"));
		job.put("writer", "GOD");
		job.put("content", URLEncoder.encode("컨트롤러에서 json객체를 ajax로 보냈습니다. ", "UTF-8"));
		
		return job.toJSONString();
	}
	
	@RequestMapping(value="test3.do", method=RequestMethod.POST)
	public void test3Method(HttpServletResponse response) throws IOException {
		List<User> list = new ArrayList<>();
		
		list.add(new User("user01","pass01","김한일",30,"user01@naver.com","01011111111"));
		list.add(new User("user02","pass02","김한이",31,"user02@naver.com","01022222222"));
		list.add(new User("user03","pass03","김한삼",32,"user03@naver.com","01033333333"));
		list.add(new User("user04","pass04","김한사",33,"user04@naver.com","01044444444"));
		list.add(new User("user05","pass05","김한오",34,"user05@naver.com","01055555555"));
		
		//전송용 객체 생성
		JSONObject sendObj = new JSONObject();
		
		//list 를 jarr 에 복사
		JSONArray jarr = new JSONArray();
		
		for(User user : list) {
			//user 객체 저장용 json 객체 생성
			JSONObject juser = new JSONObject();
			
			juser.put("userid", user.getUserid());
			juser.put("userpwd", user.getUserpwd());
			juser.put("username", user.getUsername());
			juser.put("age", user.getAge());
			juser.put("email", user.getEmail());
			juser.put("phone", user.getPhone());
			
			//jarr 에 juser 저장
			jarr.add(juser);
		}
		
		sendObj.put("list", jarr);
		
		response.setContentType("application/json; charset=utf-8");
		PrintWriter out = response.getWriter();
		out.println(sendObj.toJSONString());
		out.flush();
		out.close();
	}
	
	@RequestMapping(value="test4.do", method=RequestMethod.POST)
	public ModelAndView test4Method(ModelAndView mv) throws UnsupportedEncodingException {
		//Map 을 ModelAndView 에 담아서 JsonView 로 보냄
		
		Sample samp = new Sample("갓하늘", 5);
		samp.setName(URLEncoder.encode(samp.getName(), "UTF-8"));
		
		Map<String, Sample> map = new HashMap<String, Sample>();
		
		map.put("samp", samp);
		
		mv.addObject(map);
		//뷰지정 : 등록된 JsonView 의 id를 뷰이름으로 지정함
		mv.setViewName("jsonView");
		
		return mv;  //뷰리졸버로 전달되고, 요청한 ajax는 json 객체를 받음
	}
	
	@RequestMapping(value="test5.do", method=RequestMethod.POST)
	public ResponseEntity<String> test5Method(@RequestBody String param) throws ParseException{
		//request body 에 저장되어서 전송 온 json 문자열을 param 에 저장함
		
		//전송 온 json 문자열을 json 객체로 바꿈
		JSONParser jparser = new JSONParser();
		JSONObject job = (JSONObject)jparser.parse(param);
		
		String name = (String)job.get("name");
		int age = ((Long)job.get("age")).intValue();
		
		System.out.println(name + ", " + age);
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	}
	
	@RequestMapping(value="test6.do", method=RequestMethod.POST)
	public ResponseEntity<String> test6Method(@RequestBody String param) throws ParseException{
		//request body 에 저장되어서 전송 온 json 배열 문자열을 param 에 저장함
		
		//전송 온 json 배열 문자열을 json 배열객체로 바꿈
		JSONParser jparser = new JSONParser();
		JSONArray jarr = (JSONArray)jparser.parse(param);
		System.out.println("jarr size : " + jarr.size());
		
		for(int i = 0; i < jarr.size(); i++) {
			JSONObject job = (JSONObject)jarr.get(i);
			Sample samp = new Sample();
			samp.setName((String)job.get("name"));
			samp.setAge(((Long)job.get("age")).intValue());
			
			System.out.println(samp);
		}
		
		return new ResponseEntity<String>("success", HttpStatus.OK);
	} 
	
}
