package com.matridx.springboot.util.email;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Address;
import javax.mail.SendFailedException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.matridx.springboot.util.base.StringUtil;

@Component
public class EmailUtil {
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Value("${spring.mail.username:}")
	private String userName;
	@Value("${spring.mail.viewname:}")
	private String viewName;
	
	private final Logger log = LoggerFactory.getLogger(EmailUtil.class);
	
	/**
	 * 发送邮件
	 * @param toEmail 接收方
	 * @param subject 标题
	 * @param content 内容
	 */
	public boolean sendEmail(List<String> toEmail,String subject,String content) throws Exception {
        return sendEmail(toEmail, subject, content, null, null);
    }
	
	/**
	 * 发送邮件
	 * @param toEmail 接收方
	 * @param subject 标题
	 * @param content 内容
	 * @param filePath 文件路径
	 */
	public boolean sendEmail(List<String> toEmail,String subject,String content,String filePath, String wjm) throws Exception{
		MimeMessage message= mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true,"utf-8");
        try {
            helper.setFrom(new InternetAddress(userName, viewName, "UTF-8"));
            if(toEmail != null && !toEmail.isEmpty()){
            	helper.setTo(toEmail.get(0));
            	if(toEmail.size() > 1){
            		InternetAddress[] bcc = new InternetAddress[toEmail.size()-1];
                	for (int i = 0; i < toEmail.size(); i++) {
                		if(i > 0){
                			bcc[i-1] = new InternetAddress(toEmail.get(i));
                		}
    				}
                	helper.setBcc(bcc);
            	}
            }
            helper.setSubject(subject);
            helper.setText(content);
            //如果有文件信息
            if(StringUtil.isNotBlank(filePath)) {
	            File file = new File(filePath);
	            
	            FileSystemResource fileR=new FileSystemResource(file);
	            //String fileName= file.getName();
	            //添加多个附件可以使用多条
	            //helper.addAttachment(fileName,file);
	            helper.addAttachment(MimeUtility.encodeWord(wjm,"utf-8","B"),fileR);
            }
            mailSender.send(message);
            log.error("带附件的邮件发送成功！发送对象：" + toEmail + "  文件名："+wjm + "  文件路径："+filePath);
        }catch (MailSendException e){
            Set<String> tmpInvalidMails = getInvalidAddress(e);
            log.error("邮件发送失败，" + e.getMessage());
            // 非无效收件人导致的异常，暂不处理
            if (tmpInvalidMails.isEmpty()){
				log.error("发送带附件的邮件失败!失败原因：" + e);
//            	throw new Exception("发送带附件的邮件失败!失败原因：" + e.toString());
            }
			if (toEmail != null) {
				int oldsize = toEmail.size();
				toEmail.removeAll(tmpInvalidMails);
				if(toEmail.isEmpty()){
					log.error("邮件发送失败，无收件人" + e.getMessage());
//            	throw new Exception("发送带附件的邮件失败!失败原因：" + e.toString());
				}else {
					if (oldsize>toEmail.size()){
						//重新发送邮件
						sendEmail(toEmail,subject,content,filePath,wjm);
					}
				}
//            sendEmail(toEmail,subject,content,filePath,wjm);
			}
        }catch (Exception e){
            log.error("发送带附件的邮件失败!失败原因：" + e);
//            throw new Exception("发送带附件的邮件失败!失败原因：" + e.toString());
        }
        return true;
    }

	/**
	 * 发送邮件
	 * @param toEmail 接收方
	 * @param subject 标题
	 * @param content 内容
	 */
	public Map<String,Object> sendEmailReturnMap(List<String> toEmail, String subject, String content) throws Exception {
        return sendEmailReturnMap(toEmail, subject, content, null, null,null);
    }

	/**
	 * 发送邮件
	 * @param toEmail 接收方
	 * @param subject 标题
	 * @param content 内容
	 * @param filePath 文件路径
	 */
	public Map<String,Object> sendEmailReturnMap(List<String> toEmail,String subject,String content,String filePath, String wjm,List<String> failEmails) throws Exception{
		Map<String,Object> map = new HashMap<>();
		map.put("status","success");
		if (failEmails == null){
			failEmails = new ArrayList<>();
		}
		MimeMessage message= mailSender.createMimeMessage();
        MimeMessageHelper helper=new MimeMessageHelper(message,true,"utf-8");
        try {
            helper.setFrom(new InternetAddress(userName, viewName, "UTF-8"));
            if(toEmail != null && !toEmail.isEmpty()){
            	helper.setTo(toEmail.get(0));
            	if(toEmail.size() > 1){
            		InternetAddress[] bcc = new InternetAddress[toEmail.size()-1];
                	for (int i = 0; i < toEmail.size(); i++) {
                		if(i > 0){
                			bcc[i-1] = new InternetAddress(toEmail.get(i));
                		}
    				}
                	helper.setBcc(bcc);
            	}
            }
            helper.setSubject(subject);
            helper.setText(content);
            //如果有文件信息
            if(StringUtil.isNotBlank(filePath)) {
	            File file = new File(filePath);

	            FileSystemResource fileR=new FileSystemResource(file);
	            //String fileName= file.getName();
	            //添加多个附件可以使用多条
	            //helper.addAttachment(fileName,file);
	            helper.addAttachment(MimeUtility.encodeWord(wjm,"utf-8","B"),fileR);
            }
            mailSender.send(message);
            log.error("带附件的邮件发送成功！发送对象：" + toEmail + "  文件名："+wjm + "  文件路径："+filePath);
        }catch (MailSendException e){
            Set<String> tmpInvalidMails = getInvalidAddress(e);
            log.error("邮件发送失败，" + e.getMessage());
            // 非无效收件人导致的异常，暂不处理
            if (tmpInvalidMails.isEmpty()){
				log.error("发送带附件的邮件失败!失败原因：" + e);
//            	throw new Exception("发送带附件的邮件失败!失败原因：" + e.toString());
            }
			if (toEmail != null) {
				int oldsize = toEmail.size();
				toEmail.removeAll(tmpInvalidMails);
				failEmails.addAll(tmpInvalidMails);
				if(toEmail.isEmpty()){
					log.error("邮件发送失败，无收件人" + e.getMessage());
//            	throw new Exception("发送带附件的邮件失败!失败原因：" + e.toString());
				}else {
					if (oldsize>toEmail.size()){
						//重新发送邮件
						sendEmailReturnMap(toEmail,subject,content,filePath,wjm,failEmails);
					}
				}
//            sendEmail(toEmail,subject,content,filePath,wjm);
			}
        }catch (Exception e){
            log.error("发送带附件的邮件失败!失败原因：" + e);
//            throw new Exception("发送带附件的邮件失败!失败原因：" + e.toString());
        }
		if (!failEmails.isEmpty()){
			map.put("status","fail");
			map.put("failEmails",failEmails);
		}
        return map;
    }

	public void sendEmailThread(List<String> toEmail,String subject,String content,String filePath, String wjm) {
		Thread thread=new Thread(){
			@Override
			public void run(){
				try {
					sendEmail(toEmail, subject, content, filePath, wjm);
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
			}
		};
		thread.start();
	}
	/**
	 * 从异常信息里获取发送失败的邮箱信息
	 */
	private Set<String> getInvalidAddress(MailSendException e){
	    Set<String> mails = new HashSet<>();
	    for(Exception exception: e.getFailedMessages().values()){
	        if(exception instanceof SendFailedException){
	            for(Address address: ((SendFailedException) exception).getInvalidAddresses()){
	                mails.add(address.toString());
	            }
	        }
	    }
	    return mails;
	}
	
	public static void main(String[] args) {
		//EmailUtil emailUtil = new EmailUtil();
		//emailUtil.sendEmail("", subject, content, filePath);
	}
}
