package com.matridx.igams.wechat.service.impl;

import com.matridx.igams.common.dao.entities.JcsjDto;
import com.matridx.igams.common.dao.entities.XszbDto;
import com.matridx.igams.common.enums.BasicDataTypeEnum;
import com.matridx.igams.common.redis.RedisUtil;
import com.matridx.igams.wechat.dao.entities.FjsqDto;
import com.matridx.igams.wechat.dao.entities.SjxxDto;
import com.matridx.igams.wechat.dao.post.IQualityControlOperationsStaticDao;
import com.matridx.igams.wechat.dao.post.ISjxxDao;
import com.matridx.igams.wechat.dao.post.ISjxxStatisticDao;
import com.matridx.igams.wechat.service.svcinterface.ISjxxStatisticService;
import com.matridx.springboot.util.base.StringUtil;
import com.matridx.springboot.util.date.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SjxxStatisticServiceImpl  implements ISjxxStatisticService
{
	@Autowired
	private ISjxxStatisticDao dao;
	@Autowired
	private ISjxxDao sjxxDao;
	@Autowired
	private RedisUtil redisUtil;
	@Autowired
	IQualityControlOperationsStaticDao qualityControlOperationsStaticDao;
	private Logger log = LoggerFactory.getLogger(SjxxServiceImpl.class);
	// 微信发送日报 周报 统计

	/**
	 * 微信统计按照标本个数进行统计
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxDaily(sjxxDto);
	}
	/**
	 * 微信统计按照标本个数进行统计(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDailyByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxDailyByJsrq(sjxxDto);
	}

	/**
	 * 微信统计按照合作伙伴统计标本标本个数
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxBySjhbDaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		/*if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmc(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()!=null&&sjxxDto.getDbs().size()>0) {
			sjhbList = dao.getYbxxBySjhbDaily(sjxxDto);
			sjxxDto.setDbs(null);
		}*/
		return dao.getYbxxBySjhbDaily(sjxxDto);
	}
	/**
	 * 微信统计按照合作伙伴统计标本标本个数(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxBySjhbDailyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getYbxxBySjhbDailyAndJsrq(sjxxDto);
	}

	/**
	 * 微信统计统计复检信息
	 * 
	 * @param fjsqDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getFjsqDaily(FjsqDto fjsqDto)
	{
		// TODO Auto-generated method stub
		return dao.getFjsqDaily(fjsqDto);
	}

	/**
	 * 微信统计废弃标本
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getFqybByYbztDaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getFqybByYbztDaily(sjxxDto);
	}

	/**
	 * 微信统计报告阳性率
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJyjgDaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJyjgDaily(sjxxDto);
	}

	/**
	 * 微信统计送检清单
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getSjxxListDaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSjxxListDaily(sjxxDto);
	}
	/**
	 * 微信统计送检清单(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<SjxxDto> getSjxxListDailyByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSjxxListDailyByJsrq(sjxxDto);
	}

	/**
	 * 微信统计检测项目次数
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJcxmSumDaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJcxmSumDaily(sjxxDto);
	}
	/**
	 * 微信统计检测项目次数(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJcxmSumDailyByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJcxmSumDailyByJsrq(sjxxDto);
	}

	/**
	 * 微信统计收费项目检测项目条数
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJcxmInSfsfDaily(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJcxmInSfsfDaily(sjxxDto);
	}

    /**
	 * 微信统计收费项目检测项目条数(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJcxmInSfsfDailyByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJcxmInSfsfDailyByJsrq(sjxxDto);
	}


    /**
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSfsfByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSfsfByWeekly(sjxxDto);
	}
	/**
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSfsfByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSfsfByWeeklyAndJsrq(sjxxDto);
	}
	
	/**
	 * 微信统计标本数信息————按照周统计（周报）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getybxx_weekByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> listMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> resultMaps = dao.getybxx_weekByWeekly(sjxxDto);

			String s_preRq = "";
			int sum = 0, presum = 0;
			for (int i = 0; i < resultMaps.size(); i++)
			{
				// 日期有变化
				if (!s_preRq.equals(resultMaps.get(i).get("rq")))
				{
					if (presum == 0 && i != 0)
					{
						resultMaps.get(i - 1).put("bl", "0");
					} else if (presum != 0)
					{
						BigDecimal bg_sum = new BigDecimal(presum);
						resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
					}

					presum = sum;
					s_preRq = resultMaps.get(i).get("rq");

					if ("1".equals(resultMaps.get(i).get("sfjs")))
					{
						sum = Integer.parseInt(resultMaps.get(i).get("num"));
					} else
					{
						sum = 0;
					}
				} else if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum += Integer.parseInt(resultMaps.get(i).get("num"));
				}

			}
			if (presum == 0 && resultMaps.size() > 0)
			{
				resultMaps.get(resultMaps.size() - 1).put("bl", "0");
			} else if (presum != 0)
			{
				BigDecimal bg_sum = new BigDecimal(presum);
				resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
			}

			return resultMaps;
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return listMap;
	}
	/**
	 * 微信统计标本数信息————按照周统计（周报）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getybxx_weekByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> listMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			List<Map<String, String>> resultMaps = dao.getybxx_weekByWeeklyAndJsrq(sjxxDto);

			String s_preRq = "";
			int sum = 0, presum = 0;
			for (int i = 0; i < resultMaps.size(); i++)
			{
				// 日期有变化
				if (!s_preRq.equals(resultMaps.get(i).get("rq")))
				{
					if (presum == 0 && i != 0)
					{
						resultMaps.get(i - 1).put("bl", "0");
					} else if (presum != 0)
					{
						BigDecimal bg_sum = new BigDecimal(presum);
						resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
					}

					presum = sum;
					s_preRq = resultMaps.get(i).get("rq");

					if ("1".equals(resultMaps.get(i).get("sfjs")))
					{
						sum = Integer.parseInt(resultMaps.get(i).get("num"));
					} else
					{
						sum = 0;
					}
				} else if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum += Integer.parseInt(resultMaps.get(i).get("num"));
				}

			}
			if (presum == 0 && resultMaps.size() > 0)
			{
				resultMaps.get(resultMaps.size() - 1).put("bl", "0");
			} else if (presum != 0)
			{
				BigDecimal bg_sum = new BigDecimal(presum);
				resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
			}

			return resultMaps;
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return listMap;
	}
	
	/**
	 * 根据开始日期和结束日期，自动计算每一天的日期，形成一个list
	 * 
	 * @param sjxxDto
	 * @return
	 */
	public List<String> getRqsByStartAndEnd(SjxxDto sjxxDto)
	{
		List<String> rqs = new ArrayList<>();
		try
		{
			if ("day".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
				} else
				{
					endcalendar.setTime(new Date());
				}
				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.DATE, 1);
				}
			} else if ("mon".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqMstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqMend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqMend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.MONTH, 1);
				}
			} else if ("year".equals(sjxxDto.getTj()))
			{
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(sdf.parse(sjxxDto.getJsrqYstart()));

				Calendar endcalendar = Calendar.getInstance();
				if (StringUtil.isNotBlank(sjxxDto.getJsrqYend()))
				{
					endcalendar.setTime(sdf.parse(sjxxDto.getJsrqYend()));
				} else
				{
					endcalendar.setTime(new Date());
				}

				while (endcalendar.compareTo(calendar) >= 0)
				{
					rqs.add(sdf.format(calendar.getTime()));
					calendar.add(Calendar.YEAR, 1);
				}
			}
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return rqs;
	}
	
	/**
	 * 微信统计标本数信息（周报）
	 * 
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		List<Map<String, String>> resultMaps = dao.getYbxxByWeekly(sjxxDto);

		String s_preRq = "";
		long sum = 0, presum = 0;
		for (int i = 0; i < resultMaps.size(); i++)
		{
			// 日期有变化
			if (!s_preRq.equals(resultMaps.get(i).get("rq")))
			{
				if (presum == 0 && i != 0)
				{
					resultMaps.get(i - 1).put("bl", "0");
				} else if (presum != 0)
				{
					BigDecimal bg_sum = new BigDecimal(presum);
					resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
				}

				presum = sum;

				s_preRq = resultMaps.get(i).get("rq");

				if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum = Integer.parseInt(resultMaps.get(i).get("num"));
				} else
				{
					sum = 0;
				}
			} else if ("1".equals(resultMaps.get(i).get("sfjs")))
			{
				sum += Long.parseLong(resultMaps.get(i).get("num"));
			}

		}
		if (presum == 0 && resultMaps.size() > 0)
		{
			resultMaps.get(resultMaps.size() - 1).put("bl", "0");
		} else if (presum != 0)
		{
			BigDecimal bg_sum = new BigDecimal(presum);
			resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
		}
		return resultMaps;
	}/**
	 * 微信统计标本数信息（周报）(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		List<Map<String, String>> resultMaps = dao.getYbxxByWeeklyAndJsrq(sjxxDto);

		String s_preRq = "";
		long sum = 0, presum = 0;
		for (int i = 0; i < resultMaps.size(); i++)
		{
			// 日期有变化
			if (!s_preRq.equals(resultMaps.get(i).get("rq")))
			{
				if (presum == 0 && i != 0)
				{
					resultMaps.get(i - 1).put("bl", "0");
				} else if (presum != 0)
				{
					BigDecimal bg_sum = new BigDecimal(presum);
					resultMaps.get(i - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
				}

				presum = sum;

				s_preRq = resultMaps.get(i).get("rq");

				if ("1".equals(resultMaps.get(i).get("sfjs")))
				{
					sum = Integer.parseInt(resultMaps.get(i).get("num"));
				} else
				{
					sum = 0;
				}
			} else if ("1".equals(resultMaps.get(i).get("sfjs")))
			{
				sum += Long.parseLong(resultMaps.get(i).get("num"));
			}

		}
		if (presum == 0 && resultMaps.size() > 0)
		{
			resultMaps.get(resultMaps.size() - 1).put("bl", "0");
		} else if (presum != 0)
		{
			BigDecimal bg_sum = new BigDecimal(presum);
			resultMaps.get(resultMaps.size() - 1).put("bl", new BigDecimal(sum).subtract(bg_sum).multiply(new BigDecimal("100")).divide(bg_sum, 0, RoundingMode.HALF_UP).toString());
		}
		return resultMaps;
	}
	
	/**
	 * 微信统计测试数信息————D+R的算两个（周）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDRByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
        return dao.getYbxxDRByWeekly(sjxxDto);
	}
	/**
	 * 微信统计测试数信息————D+R的算两个（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDRByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
        return dao.getYbxxDRByWeeklyAndJsrq(sjxxDto);
	}

	/**
	 * 微信周统计测试数信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDR_weeek(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getYbxxDR_weeek(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 微信周统计测试数信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxDR_weeekByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getYbxxDR_weeekByJsrq(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 微信统计收费标本的测试数信息（周）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxTypeByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
        return dao.getYbxxTypeByWeekly(sjxxDto);
	}
	/**
	 * 微信统计收费标本的测试数信息（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxTypeByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
        return dao.getYbxxTypeByWeeklyAndJsrq(sjxxDto);
	}

	/**
	 * 微信按照周统计收费标本的测试数信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxType_week(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getYbxxType_week(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	/**
	 * 微信按照周统计收费标本的测试数信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxType_weekByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		// TODO Auto-generated method stub
		List<Map<String, String>> resultMap = null;
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			resultMap = dao.getYbxxType_weekByJsrq(sjxxDto);
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return resultMap;
	}
	
	/**
	 * 微信统计上上周的临床反馈结果
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getLcfkOfBeforeWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		String jsrqstart = getBeforeWeek(sjxxDto.getJsrqstart(), -14);
		String jsrqend = getBeforeWeek(sjxxDto.getJsrqend(), -14);
		sjxxDto.setJsrqstart(jsrqstart);
		sjxxDto.setJsrqend(jsrqend);
		List<Map<String, String>> listMap = dao.getLcfkOfBeforeWeekly(sjxxDto);
		sjxxDto.getZqs().put("lcfkList", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.setJsrqstart(getBeforeWeek(sjxxDto.getJsrqstart(), +14));
		sjxxDto.setJsrqend(getBeforeWeek(sjxxDto.getJsrqend(), +14));
		return listMap;
	}

	/**
	 * 微信统计标本前三的阳性率（周）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJyjgWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJyjgWeekly(sjxxDto);
	}

	/**
	 * 微信统计科室（周）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getKsByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getKsByWeekly(sjxxDto);
	}

	/**
	 * 微信统计科室（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getKsByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getKsByWeeklyAndJsrq(sjxxDto);
	}

	/**
	 * 根据日期返回两周前的日期
	 * 
	 * @param data
	 * @return
	 */
	public static String getBeforeWeek(String data, int day)
	{
		String dataFormat = null;
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(data));
			calendar.add(Calendar.DATE, day);
			dataFormat = sdf.format(calendar.getTime());
		} catch (ParseException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return dataFormat;
	}
	
	/**
	 * 微信统计合作医疗机构
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getCooperativeWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> strList = new ArrayList<>();
		strList.add("医院");
		strList.add("医师");
		strList.add("科室");
		List<Map<String, String>> ToWeek = dao.getCooperativeWeekly(sjxxDto);
		String jsrqend = getBeforeWeek(sjxxDto.getJsrqend(), -7);
		sjxxDto.setJsrqend(jsrqend);
		List<Map<String, String>> beforeWeek = dao.getCooperativeWeekly(sjxxDto);
		List<Map<String, String>> ListMap = new ArrayList<>();
		for (int i = 0; i < ToWeek.size(); i++)
		{
			Map<String, String> map1 = new HashMap<>();
			String before = String.valueOf(beforeWeek.get(i).get("num"));
			String now = String.valueOf(ToWeek.get(i).get("num"));
			int more = Integer.parseInt(now) - Integer.parseInt(before);
			map1.put("hzdw", strList.get(i));
			map1.put("before", before);
			map1.put("now", now);
			map1.put("more", more + "");
			ListMap.add(map1);
		}
		sjxxDto.getZqs().put("sjdwOfsjysOfks", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.setJsrqend(getBeforeWeek(sjxxDto.getJsrqend(), +7));
		return ListMap;
	}
	/**
	 * 微信统计合作医疗机构(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getCooperativeWeeklyByJSrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> strList = new ArrayList<>();
		strList.add("医院");
		strList.add("医师");
		strList.add("科室");
		List<Map<String, String>> ToWeek = dao.getCooperativeWeeklyByJSrq(sjxxDto);
		String jsrqend = getBeforeWeek(sjxxDto.getJsrqend(), -7);
		sjxxDto.setJsrqend(jsrqend);
		List<Map<String, String>> beforeWeek = dao.getCooperativeWeeklyByJSrq(sjxxDto);
		List<Map<String, String>> ListMap = new ArrayList<>();
		for (int i = 0; i < ToWeek.size(); i++)
		{
			Map<String, String> map1 = new HashMap<>();
			String before = String.valueOf(beforeWeek.get(i).get("num"));
			String now = String.valueOf(ToWeek.get(i).get("num"));
			int more = Integer.parseInt(now) - Integer.parseInt(before);
			map1.put("hzdw", strList.get(i));
			map1.put("before", before);
			map1.put("now", now);
			map1.put("more", more + "");
			ListMap.add(map1);
		}
		sjxxDto.getZqs().put("sjdwOfsjysOfks", sjxxDto.getJsrqstart() + "~" + sjxxDto.getJsrqend());
		sjxxDto.setJsrqend(getBeforeWeek(sjxxDto.getJsrqend(), +7));
		return ListMap;
	}

	/**
	 * 按照周对合作伙伴进行统计
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjhbByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSjhbByWeekly(sjxxDto);
	}
	/**
	 * 按照周对合作伙伴进行统计(接收日期)
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjhbByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getSjhbByWeeklyAndJsrq(sjxxDto);
	}
	
	/**
	 * 获取日期和标本数量
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getDataOfNum(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getYblxByJsrq(sjxxDto);
	}
	/**
	 * 获取日期和标本数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getDataOfNumByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getYblxByJsrqAndJsrq(sjxxDto);
	}
	
	/**
	 * 微信统计报告数量（周）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getSjbgByBgrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getSjbgByBgrq(sjxxDto);
	}

	
	/**
	 * 微信统计每天标本的阳性率
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJyjgBybgrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		return dao.getJyjgBybgrq(sjxxDto);
	}
	
	/**
	 * 微信按照周统计标本个数（周）
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxnumByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			return dao.getYbxxnumByWeekly(sjxxDto);
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}
	/**
	 * 微信按照周统计标本个数（周）(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getYbxxnumByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		try
		{
			List<String> rqs = new ArrayList<>();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			return dao.getYbxxnumByWeeklyAndJsrq(sjxxDto);
		} catch (Exception e)
		{
			log.error(e.getMessage());
		}
		return null;
	}

	/**
	 * 微信统计
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getJyjgLxByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		return dao.getJyjgLxByWeekly(sjxxDto);
	}

	/**
	 * 设置返回对应ID
	 * 
	 * @param fllist
	 * @param name
	 * @return
	 */
	public Map<String, List<Map<String, String>>> setReceiveId(List<Map<String, String>> fllist, String name)
	{
		if (fllist == null)
			return null;
		Map<String, List<Map<String, String>>> flmap = new HashMap<>();
		for (int i = 0; i < fllist.size(); i++)
		{
			String csdm = fllist.get(i).get(name);
			if (flmap.get(csdm) == null)
			{
				List<Map<String, String>> t_list = new ArrayList<>();
				flmap.put(csdm, t_list);
			}
			flmap.get(csdm).add(fllist.get(i));
		}
		return flmap;
	}
	
	/**
	 * 微信周报按照日期和代表名统计报告数量
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,List<Map<String, String>>> getSjhbFlByWeekly(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmc(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null ||sjxxDto.getDbs().size()==0) 
			return null;
		List<Map<String, String>> fllist = dao.getSjhbFlByWeekly(sjxxDto);
		if (fllist == null || fllist.size() == 0)
			return null;
		Map<String, List<Map<String, String>>> flmap = setReceiveId(fllist, "fl");
		sjxxDto.setDbs(null);
		return flmap;
	}
	/**
	 * 微信周报按照日期和代表名统计报告数量(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,List<Map<String, String>>> getSjhbFlByWeeklyAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		List<Map<String, String>> fllist = dao.getSjhbFlByWeeklyAndJSrq(sjxxDto);
		if (fllist == null || fllist.size() == 0)
			return null;
		Map<String, List<Map<String, String>>> flmap = setReceiveId(fllist, "fl");
		sjxxDto.setDbs(null);
		return flmap;
	}
	
	/**
	 * 根据代表名按周查询信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,List<Map<String, String>>> getSjhbFl_week(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs()==null) {
			List<String> dbList = sjxxDao.getTjmc(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null ||sjxxDto.getDbs().size()==0) 
			return null;
		List<Map<String, String>> fllist = dao.getSjhbFl_week(sjxxDto);
		sjxxDto.setDbs(null);

		if (fllist == null || fllist.size() == 0)
			return null;
        return setReceiveId(fllist, "fl");
	}
	/**
	 * 根据代表名按周查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public Map<String,List<Map<String, String>>> getSjhbFl_weekByJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs()==null) {
			List<String> dbList = sjxxDao.getTjmcByJsrq(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null ||sjxxDto.getDbs().size()==0)
			return null;
		List<Map<String, String>> fllist = dao.getSjhbFl_weekByJsrq(sjxxDto);
		sjxxDto.setDbs(null);

		if (fllist == null || fllist.size() == 0)
			return null;
        return setReceiveId(fllist, "fl");
	}

	/**
	 * 根据统计名称名查询信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeeklyByTjFl(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmc(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null || sjxxDto.getDbs().size()==0) 
			return null;
		//如果前端有传入表头则，不替换
		if(!(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0&&sjxxDto.getDbhbs()!=null)) {
			sjxxDto.setDbmcs(sjxxDto.getDbs());
		}
		List<Map<String, String>> fllist = dao.getWeeklyByTjFl(sjxxDto);
		sjxxDto.setDbs(null);
		sjxxDto.setDbmcs(sjxxDto.getDbs());
		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}
	/**
	 * 根据统计名称名查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeeklyByTjFlAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmcByJsrq(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null || sjxxDto.getDbs().size()==0)
			return null;
		//如果前端有传入表头则，不替换
		if(!(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0&&sjxxDto.getDbhbs()!=null)) {
			sjxxDto.setDbmcs(sjxxDto.getDbs());
		}
		List<Map<String, String>> fllist = dao.getWeeklyByTjFlAndJsrq(sjxxDto);
		sjxxDto.setDbs(null);
		sjxxDto.setDbmcs(sjxxDto.getDbs());
		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}

	/**
	 * 根据统计名称名查询信息(接收日期、周)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeeklyByTjFlAndJsrqAndWeek(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmcByJsrq(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null || sjxxDto.getDbs().size()==0)
			return null;
		//如果前端有传入表头则，不替换
		if(!(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0&&sjxxDto.getDbhbs()!=null)) {
			sjxxDto.setDbmcs(sjxxDto.getDbs());
		}
		List<Map<String, String>> fllist = dao.getWeeklyByTjFlAndJsrq(sjxxDto);
		sjxxDto.setDbs(null);
		sjxxDto.setDbmcs(sjxxDto.getDbs());
		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}
	/**
	 * 根据统计名称名查询信息，报表名称
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeeklyByTjFlTm(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmc(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null || sjxxDto.getDbs().size()==0) 
			return null;
		//如果前端有传入表头则，不替换
		if(!(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0&&sjxxDto.getDbhbs()!=null)) {
			sjxxDto.setDbmcs(sjxxDto.getDbs());
		}
		List<Map<String, String>> fllist = dao.getWeeklyByTjFlTm(sjxxDto);
		sjxxDto.setDbs(null);
		sjxxDto.setDbmcs(sjxxDto.getDbs());
		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}
	/**
	 * 根据统计名称名查询信息，报表名称(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeeklyByTjFlTmAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmcByJsrq(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null || sjxxDto.getDbs().size()==0)
			return null;
		//如果前端有传入表头则，不替换
		if(!(sjxxDto.getDbmcs()!=null&&sjxxDto.getDbmcs().size()>0&&sjxxDto.getDbhbs()!=null)) {
			sjxxDto.setDbmcs(sjxxDto.getDbs());
		}
		List<Map<String, String>> fllist = dao.getWeeklyByTjFlTmAndJsrq(sjxxDto);
		sjxxDto.setDbs(null);
		sjxxDto.setDbmcs(sjxxDto.getDbs());
		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}
	/**
	 * 根据统计名称按周查询信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeeklyByTjFl_week(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmc(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null || sjxxDto.getDbs().size()==0) 
			return null;
		List<Map<String, String>> fllist = dao.getWeeklyByTjFl_week(sjxxDto);
		sjxxDto.setDbs(null);
		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}
	/**
	 * 根据统计名称按周查询信息(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, String>> getWeeklyByTjFl_weekAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);
		if(sjxxDto.getDbs() ==null) {
			List<String> dbList = sjxxDao.getTjmcByJsrq(sjxxDto);
			if(dbList!=null) {
				sjxxDto.setDbs(dbList);
			}
		}
		if(sjxxDto.getDbs()==null || sjxxDto.getDbs().size()==0)
			return null;
		List<Map<String, String>> fllist = dao.getWeeklyByTjFl_weekAndJsrq(sjxxDto);
		sjxxDto.setDbs(null);
		if (fllist == null || fllist.size() == 0)
			return null;
		return fllist;
	}
	
	/**
	 * 领导周报，销售各分类的检测量(本sql区别getStatisByFl ，主要是为了减少数据量)
	 * @param sjxxDto
	 * @return
	 */
	public Map<String,List<Map<String, String>>> getStatisByFlLimit(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByFlLimit(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
        return setReceiveId(fllist, "flid");
	}
	/**
	 * 领导周报，销售各分类的检测量(本sql区别getStatisByFl ，主要是为了减少数据量)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public Map<String,List<Map<String, String>>> getStatisByFlLimitAndJsrq(SjxxDto sjxxDto){
		// TODO Auto-generated method stub
		List<String> rqs = getRqsByStartAndEnd(sjxxDto);
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByFlLimitAndJsrq(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
        return setReceiveId(fllist, "flid");
	}
	
	/**
	 * 根据周的伙伴分类，伙伴子分类，代表名查询送检信息
	 * 
	 * @param sjxxDto
	 * @return
	 */
	public Map<String, List<Map<String, String>>> getStatisByWeekFlLimit(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByWeekFlLimit(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
        return setReceiveId(fllist, "flid");
	}
	/**
	 * 根据周的伙伴分类，伙伴子分类，代表名查询送检信息(接收日期)
	 *
	 * @param sjxxDto
	 * @return
	 */
	public Map<String, List<Map<String, String>>> getStatisByWeekFlLimitAndJsrq(SjxxDto sjxxDto)
	{
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		// 设置日期
		try
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(sdf.parse(sjxxDto.getJsrqstart()));

			int zq = Integer.parseInt(sjxxDto.getZq());
			for (int i = 0; i < zq; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
		} catch (Exception ex)
		{
			log.error(ex.toString());
		}
		sjxxDto.setRqs(rqs);

		List<Map<String, String>> fllist = dao.getStatisByWeekFlLimitAndJsrq(sjxxDto);

		if (fllist == null || fllist.size() == 0)
			return null;
        return setReceiveId(fllist, "flid");
	}
	
	/**
	 * 领导周报，各省份的检测量数据统计(统计一周)，用于显示在省份名称的旁边)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSfStatisByWeek(SjxxDto sjxxDto){
		return dao.getSfStatisByWeek(sjxxDto);
	}
	/**
	 * 领导周报，各省份的检测量数据统计(统计一周)，用于显示在省份名称的旁边)(接收日期)
	 * @param sjxxDto
	 * @return
	 */
	public List<Map<String, String>> getSfStatisByWeekAndJsrq(SjxxDto sjxxDto){
		return dao.getSfStatisByWeekAndJsrq(sjxxDto);
	}

	/**
	 * 查询检测单位分组
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSjxxByJcdw(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getSjxxByJcdw(sjxxDto);
	}

	/**
	 * 根据检测单位查询统计数据
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSjxxInJcdw(SjxxDto sjxxDto) {	
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		if("week".equals(sjxxDto.getTj())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			sjxxDto.setWeek_flag("1");
		}else {
			rqs = getRqsByStartAndEnd(sjxxDto);
			sjxxDto.setRqs(rqs);
		}
        return dao.getSjxxInJcdw(sjxxDto);
	}

	/**
	 * 通过省份查询送检信息
	 * @param sjxxDto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getSjxxBySfPie(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getSjxxBySfPie(sjxxDto);
	}

	@Override
	public List<Map<String, Object>> getWsjByJcdw(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getWsjByJcdw(sjxxDto);
	}

	@Override
	public List<Map<String, Object>> getSjxxInJcdwByWsj(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		List<String> rqs = new ArrayList<>();
		if("week".equals(sjxxDto.getTj())) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Calendar calendar = Calendar.getInstance();
			try {
				calendar.setTime(sdf.parse(sjxxDto.getJsrqend()));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			for (int i = 0; i < 5; i++)
			{
				rqs.add(sdf.format(calendar.getTime()));
				calendar.add(Calendar.DATE, -7);
			}
			sjxxDto.setRqs(rqs);
			sjxxDto.setWeek_flag("1");
		}else {
			rqs = getRqsByStartAndEnd(sjxxDto);
			sjxxDto.setRqs(rqs);
		}
        return dao.getSjxxInJcdwByWsj(sjxxDto);
	}

	@Override
	public List<SjxxDto> getSjxxInJcdwByWsjList(SjxxDto sjxxDto) {
		return dao.getSjxxInJcdwByWsjList(sjxxDto);
	}

	@Override
	public List<SjxxDto> getNotSyListDaily(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getNotSyListDaily(sjxxDto);
	}
	@Override
	public List<SjxxDto> getNotSyListDailyByJsrq(SjxxDto sjxxDto) {
		// TODO Auto-generated method stub
		return dao.getNotSyListDailyByJsrq(sjxxDto);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getSalesAttainmentRate(XszbDto xszbDto) {
		//查询日期处理
		setSalesAttainmentRateQueryRqs(xszbDto);
		List<XszbDto> xszbDtoList = dao.getSalesAttainmentRate(xszbDto);
		Map<String,Object> map = new HashMap<>();
		//按区域重组数据
		for (XszbDto xszb : xszbDtoList) {
			xszb.setZblxcsmc(xszbDto.getZblxcsmc());
			List obj = (List) map.get(xszb.getQyid());
			if (obj == null) {
				map.put(xszb.getQyid(), new ArrayList<>());
				obj = (List) map.get(xszb.getQyid());
			}
			//重新封装数据
			if ("M".equals(xszbDto.getZblxcsmc())) { //月设置时间进度
				Calendar calendar = Calendar.getInstance();
				String currentDate = DateUtils.format(new Date(), "yyyy-MM");
				if (currentDate.equals(xszb.getKszq())) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));//当前天
				} else {
					Date rq = DateUtils.parseDate("yyyy-MM", xszb.getKszq());
					calendar.setTime(rq);
					xszb.setDqts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));//当前在第几天
				}
				xszb.setZts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));//总天数
			} else if ("Q".equals(xszbDto.getZblxcsmc())) { //季度设置时间进度
				String startDate = xszb.getKszq();
				int curMonth = Calendar.getInstance().get(Calendar.MONTH);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate("yyyy-MM", startDate));
				int zts = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				boolean flag = false;
				if (curMonth == calendar.get(Calendar.MONTH)) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
					flag = true;
				}
				calendar.add(Calendar.MONTH, 1);
				if (curMonth == calendar.get(Calendar.MONTH)) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+zts));
					flag = true;
				}
				zts += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				calendar.add(Calendar.MONTH, 1);
				if (curMonth == calendar.get(Calendar.MONTH)) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+zts));
					flag = true;
				}
				zts += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				xszb.setZts(String.valueOf(zts));
				if(!flag) {
					xszb.setDqts(String.valueOf(zts));
				}
			} else if ("Y".equals(xszbDto.getZblxcsmc())) { //年设置时间进度
				Calendar calendar = Calendar.getInstance();
				String currentDate = DateUtils.format(new Date(), "yyyy");
				if (currentDate.equals(xszb.getKszq())) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_YEAR)));//当前天
				} else {
					Date rq = DateUtils.parseDate("yyyy", xszb.getKszq());
					calendar.setTime(rq);
					xszb.setDqts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_YEAR)));//当前在第几天
				}
				xszb.setZts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_YEAR)));//总天数
			}
			obj.add(xszb);
		}
		return  map;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Map<String, Object> getSalesAttainmentRateByJsrq(XszbDto xszbDto) {
		//查询日期处理
		setSalesAttainmentRateQueryRqs(xszbDto);
		List<XszbDto> xszbDtoList = dao.getSalesAttainmentRateByJsrq(xszbDto);
		Map<String,Object> map = new HashMap<>();
		//按区域重组数据
		for (XszbDto xszb : xszbDtoList) {
			xszb.setZblxcsmc(xszbDto.getZblxcsmc());
			List obj = (List) map.get(xszb.getQyid());
			if (obj == null) {
				map.put(xszb.getQyid(), new ArrayList<>());
				obj = (List) map.get(xszb.getQyid());
			}
			//重新封装数据
			if ("M".equals(xszbDto.getZblxcsmc())) { //月设置时间进度
				Calendar calendar = Calendar.getInstance();
				String currentDate = DateUtils.format(new Date(), "yyyy-MM");
				if (currentDate.equals(xszb.getKszq())) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));//当前天
				} else {
					Date rq = DateUtils.parseDate("yyyy-MM", xszb.getKszq());
					calendar.setTime(rq);
					xszb.setDqts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));//当前在第几天
				}
				xszb.setZts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_MONTH)));//总天数
			} else if ("Q".equals(xszbDto.getZblxcsmc())) { //季度设置时间进度
				String startDate = xszb.getKszq();
				int curMonth = Calendar.getInstance().get(Calendar.MONTH);
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(DateUtils.parseDate("yyyy-MM", startDate));
				int zts = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				boolean flag = false;
				if (curMonth == calendar.get(Calendar.MONTH)) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));
					flag = true;
				}
				calendar.add(Calendar.MONTH, 1);
				if (curMonth == calendar.get(Calendar.MONTH)) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+zts));
					flag = true;
				}
				zts += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				calendar.add(Calendar.MONTH, 1);
				if (curMonth == calendar.get(Calendar.MONTH)) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)+zts));
					flag = true;
				}
				zts += calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
				xszb.setZts(String.valueOf(zts));
				if(!flag) {
					xszb.setDqts(String.valueOf(zts));
				}
			} else if ("Y".equals(xszbDto.getZblxcsmc())) { //年设置时间进度
				Calendar calendar = Calendar.getInstance();
				String currentDate = DateUtils.format(new Date(), "yyyy");
				if (currentDate.equals(xszb.getKszq())) {
					xszb.setDqts(String.valueOf(calendar.get(Calendar.DAY_OF_YEAR)));//当前天
				} else {
					Date rq = DateUtils.parseDate("yyyy", xszb.getKszq());
					calendar.setTime(rq);
					xszb.setDqts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_YEAR)));//当前在第几天
				}
				xszb.setZts(String.valueOf(calendar.getActualMaximum(Calendar.DAY_OF_YEAR)));//总天数
			}
			obj.add(xszb);
		}
		return  map;
	}
	public void setSalesAttainmentRateQueryRqs(XszbDto xszbDto){
		String kszq = xszbDto.getKszq();
		String jszq = xszbDto.getJszq();
		List<Map<String, String>> list = new ArrayList<>();
		xszbDto.setRqs(list);
		Map<String, String> rq = new HashMap<>();
		Calendar startCalendar = Calendar.getInstance();//开始
		if("Q".equals(xszbDto.getZblxcsmc())){
			if(kszq!=null&&kszq.equals(jszq)) {//全年
				startCalendar.setTime(DateUtils.parseDate("yyyy",kszq));
				rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-01");
				rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-03");
				xszbDto.getRqs().add(rq);
				rq = new HashMap<>();
				rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-04");
				rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-06");
				xszbDto.getRqs().add(rq);
				rq = new HashMap<>();
				rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-07");
				rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-09");
				xszbDto.getRqs().add(rq);
				rq = new HashMap<>();
				rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-10");
				rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-12");
				xszbDto.getRqs().add(rq);
			}else if(kszq!=null&&!kszq.equals(jszq)) { //当前季度
				startCalendar.setTime(DateUtils.parseDate("yyyy-MM", kszq));
				int curMonth = startCalendar.get(Calendar.MONTH)+1;//获得月份
				if(curMonth<=3) {
					rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-01");
					rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-03");
					xszbDto.getRqs().add(rq);
				}else if(curMonth<=6) {
					rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-04");
					rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-06");
					xszbDto.getRqs().add(rq);
				}else if(curMonth<=9) {
					rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-07");
					rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-09");
					xszbDto.getRqs().add(rq);
				}else if(curMonth<=12) {
					rq.put("kszq",startCalendar.get(Calendar.YEAR)+"-10");
					rq.put("jszq",startCalendar.get(Calendar.YEAR)+"-12");
					xszbDto.getRqs().add(rq);
				}
			}
			
			return;
		}
		Calendar endCalendar = Calendar.getInstance();//结束
		String formate = "";
		if("M".equals(xszbDto.getZblxcsmc())){
			formate = "yyyy-MM";
		}else if("Y".equals(xszbDto.getZblxcsmc())){
			formate = "yyyy";
		}
		Date ksDate = DateUtils.parseDate(formate, kszq);
		Date jsDate = DateUtils.parseDate(formate, jszq);
		startCalendar.setTime(ksDate);
		endCalendar.setTime(jsDate);
		int flag = startCalendar.compareTo(endCalendar);
		if(flag>=0){ //开始时间大于或等于结束时间
			rq.put("kszq",DateUtils.format(startCalendar.getTime(),formate));
			rq.put("jszq",DateUtils.format(startCalendar.getTime(),"yyyy-MM"));
			xszbDto.getRqs().add(rq);
			return;
		}
		while (startCalendar.compareTo(endCalendar)<=0){
			rq = new HashMap<>();
			rq.put("kszq",DateUtils.format(startCalendar.getTime(),formate));
			rq.put("jszq",DateUtils.format(startCalendar.getTime(),formate));
			xszbDto.getRqs().add(rq);
			if ("M".equals(xszbDto.getZblxcsmc())) {
				startCalendar.add(Calendar.MONTH,1);
			}else if ("Y".equals(xszbDto.getZblxcsmc())){
				startCalendar.add(Calendar.YEAR,1);
			}
		}
	}
	@Override
	public List<Map<String, String>> getSalesAttainmentRateByArea(XszbDto xszbDto) {
		return dao.getSalesAttainmentRateByArea(xszbDto);
	}
	@Override
	public List<Map<String, String>> getSalesAttainmentRateByAreaAndJsrq(XszbDto xszbDto) {
		return dao.getSalesAttainmentRateByAreaAndJsrq(xszbDto);
	}
	
	/**
	 * 查询指定时间内的伙伴测试数
	 * @param sjxxDto
	 * @return
	 */
    public List<Map<String, String>> getStatisBySomeTimeDB(SjxxDto sjxxDto){
    	return dao.getStatisBySomeTimeDB(sjxxDto);
    }

    /**
	 * 查询指定时间内的伙伴测试数(接收日期)
	 * @param sjxxDto
	 * @return
	 */
    public List<Map<String, String>> getStatisBySomeTimeDBAndJsrq(SjxxDto sjxxDto){
    	return dao.getStatisBySomeTimeDBAndJsrq(sjxxDto);
    }

    /**
	 * 统计标本质量合格率
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getQualifiedRateByRq(Map<String,Object> map){
		return dao.getQualifiedRateByRq(map);
	}
	/**
	 * 统计各标本类型的不合格率
	 * @param map
	 * @return
	 */
	public List<Map<String,Object>> getDisqualificationRateWithTypeByRq(Map<String,Object> map){
		return dao.getDisqualificationRateWithTypeByRq(map);
	}

	/**
	 * 获取质控营销统计数据
	 * @param map
	 * @return
	 */
	public Map<String,Object> getMarketStstictisData(Map<String,Object> map){
		Map<String, Object> returnmap = new HashMap<>();
		if(StringUtil.isBlank(map.get("moneyflag").toString())){
			//营销统计主要统计数目
			if(StringUtil.isBlank(map.get("flag").toString())) {
				//伙伴分类
				List<JcsjDto> hbfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode());
				//新增第三方统计
				List<String> hbfls_totalpal_t=new ArrayList<>();
				for (JcsjDto jcsjDto : hbfllist){
					if (("001".equals(jcsjDto.getCsdm()))){
						hbfls_totalpal_t.add(jcsjDto.getCsid());
					}
				}
				map.put("hbfls_totalpal",hbfls_totalpal_t);
				List<Map<String, Object>> thpartpallist = dao.getNewPartnertotal(map);
				returnmap.put("thpartpal", thpartpallist);
				List<String> hbfls = new ArrayList<>();
				for (JcsjDto jcsjDto : hbfllist) {
					if ("001".equals(jcsjDto.getCsdm()) || "002".equals(jcsjDto.getCsdm()) || "003".equals(jcsjDto.getCsdm()) || "004".equals(jcsjDto.getCsdm())) {
						hbfls.add(jcsjDto.getCsid());
					}
				}
				map.put("hbfls", hbfls);
				//1.1、渠道收费情况
				List<Map<String, Object>> channelChargeList = dao.getSfChannelChargeData(map);
				List<Map<String, Object>> channelChargeNew = new ArrayList<>();
				if (!CollectionUtils.isEmpty(channelChargeList)) {
					List<String> list = new ArrayList<>();
					for (Map<String, Object> channel : channelChargeList) {
						if (!CollectionUtils.isEmpty(channel)) {
							String rq = channel.get("rq").toString();
							Map<String, Object> map_n = new HashMap<>();
							if (!list.contains(rq)) {
								for (Map<String, Object> map_m : channelChargeList) {
									if (rq.equals(map_m.get("rq").toString())) {
										map_n.put(map_m.get("qdmc")==null?"":map_m.get("qdmc").toString(), map_m.get("sfnum"));
									}
								}
								map_n.put("rq", rq);
								channelChargeNew.add(map_n);
							}
							list.add(rq);
						}
					}
				}
				returnmap.put("channelChargeList", channelChargeNew);
				//1.2、业务类型收费情况
				List<Map<String, Object>> businessTypeChargeList = dao.getSfBusinessTypeChargeData(map);//==============改完
				List<Map<String, Object>> businessTypeNew = new ArrayList<>();
				if (!CollectionUtils.isEmpty(businessTypeChargeList)) {
					List<String> list = new ArrayList<>();
					for (Map<String, Object> businessTypeCharge : businessTypeChargeList) {
						if (!CollectionUtils.isEmpty(businessTypeCharge)) {
							String rq = businessTypeCharge.get("rq").toString();
							if (!list.contains(rq)) {
								Map<String, Object> map_n = new HashMap<>();
								for (Map<String, Object> map_m : businessTypeChargeList) {
									if (rq.equals(map_m.get("rq").toString())) {
										map_n.put(map_m.get("qfmc").toString(), map_m.get("sfnum"));
									}
								}
								map_n.put("rq", rq);
								businessTypeNew.add(map_n);
							}
							list.add(rq);
						}
					}
				}
				returnmap.put("businessTypeChargeList", businessTypeNew);
				//1.3、渠道免费占比
				List<Map<String, Object>> channelChargeSfMfList = dao.getSfMfChannelChargeData(map);
				returnmap.put("channelChargeSfMfList", channelChargeSfMfList);
				//1.4、业务类型免费占比
				List<Map<String, Object>> businessTypeChargeSfMfList = dao.getSfMfBusinessTypeChargeData(map);
				returnmap.put("businessTypeChargeSfMfList", businessTypeChargeSfMfList);
				//1.5、直销top10排行
				returnmap.put("topzx",dao.getZx(map));
				//1.6、直销核心医院占比
				returnmap.put("zxzdyyList", dao.getCentralHospitalData(map));
				//1.7、医院送检排行
				List<String> hbfls_t = new ArrayList<>();
				for (JcsjDto jcsjDto : hbfllist) {
					if ("002".equals(jcsjDto.getCsdm()) || "003".equals(jcsjDto.getCsdm()) || "004".equals(jcsjDto.getCsdm())) {
						hbfls_t.add(jcsjDto.getCsid());
					}
				}
                map.put("hbfls", hbfls_t);
				List<Map<String, Object>> yyxxphlist = dao.getHospitalSales(map);
				List<Map<String, Object>> yyxxphlist_t = new ArrayList<>();
				if (!CollectionUtils.isEmpty(yyxxphlist)) {
					for (Map<String, Object> hospital : yyxxphlist) {
						Map<String, Object> returnMap = new HashMap<>();
						returnMap.put("dwmc", hospital.get("dwmc"));
						returnMap.put("zh", hospital.get("zh"));
						returnMap.put("yyzddj", hospital.get("yyzddj"));
						returnMap.put("zxsfnum", hospital.get("zxsfnum"));
						returnMap.put("dlssfnum", hospital.get("dlssfnum"));
						returnMap.put("dqxx", hospital.get("dqxx"));
						yyxxphlist_t.add(returnMap);
					}
				}
				returnmap.put("yyxxphlist", yyxxphlist_t);
				//1.8、核心医院送检排行(直销+代理)
				String[] yyzddjs={"A","B","C","A+"};
				map.put("yyzddjs",yyzddjs);
				List<Map<String, Object>> hxyyxxphlist = dao.getHospitalSales(map);
				List<Map<String, Object>> hxyyxxphlist_t =new ArrayList<>();
				if (!CollectionUtils.isEmpty(hxyyxxphlist)){
					for (Map<String, Object> hospital : hxyyxxphlist) {
						Map<String, Object> returnMap_t = new HashMap<>();
						returnMap_t.put("dwmc",hospital.get("dwmc"));
						returnMap_t.put("zxsfnum",hospital.get("zxsfnum"));
						returnMap_t.put("dlssfnum",hospital.get("dlssfnum"));
						returnMap_t.put("yyzddj",hospital.get("yyzddj"));
						returnMap_t.put("zh",hospital.get("zh"));
						returnMap_t.put("dqxx",hospital.get("dqxx"));
						hxyyxxphlist_t.add(returnMap_t);
					}
				}
				returnmap.put("hxyyxxphlist",hxyyxxphlist_t);
				//1.9、入院医院送检排行
				List<Map<String,Object>> ryyySfcssList = dao.getRyyySfcssTopList(map);
				returnmap.put("ryyysfcssList",ryyySfcssList);
				//2.3、月人均收费测试数（公式=累计月人均收费测试数（直销当中的特检、入院、科技）/ 过去月份）
				List<Map<String,Object>> ndyrjSfcssList = dao.getNdyrjSfcssList(map);
				returnmap.put("ndyrjsfcssList",ndyrjSfcssList);
				//2.3.1、月人均收费测试数（公式=累计月人均收费测试数（直销当中的特检）/ 过去月份）
				List<Map<String,Object>> tjndyrjsfcssList = dao.getTjNdyrjSfcssList(map);
				returnmap.put("tjndyrjsfcssList",tjndyrjsfcssList);
				//2.3.2、月人均收费测试数（公式=累计月人均收费测试数（直销当中的入院）/ 过去月份）
				List<Map<String,Object>> ryndyrjsfcssList = dao.getRyNdyrjSfcssList(map);
				returnmap.put("ryndyrjsfcssList",ryndyrjsfcssList);
				//2.4、收费科室分布（按照收费测试数统计）
				List<Map<String, Object>> ksSfcssList = dao.getNumberOfChargesDivideByKs(map);
				returnmap.put("ksSfcssList", ksSfcssList);
				//2.5、收费标本类型分布（按照收费测试数统计）
				List<Map<String, Object>> yblxSfcssList = dao.getNumberOfChargesDivideByYblx(map);
				returnmap.put("yblxSfcssList", yblxSfcssList);
				//2.6、达成率
				List<Map<String, Object>> kpiList = getKpiList(map);
				returnmap.put("kpiList", kpiList);
				//1.10、新增代理统计
				List<String> hbfls_totalpal = new ArrayList<>();
				for (JcsjDto jcsjDto : hbfllist) {
					if ("002".equals(jcsjDto.getCsdm())||"003".equals(jcsjDto.getCsdm())){
						hbfls_totalpal.add(jcsjDto.getCsid());
					}
				}
				map.put("hbfls_totalpal",hbfls_totalpal);
				returnmap.put("totalpal",dao.getNewPartnertotal(map));
				returnmap.put("newpartner",dao.getNewPartner(map));
				//1.11 第三方实验室TOP10排行
				returnmap.put("topdsf",dao.getTopDsf(map));
				//1.12 增加代理商(代理+CSO)top10统计图
				List<Map<String, Object>> topDls = dao.getTopDls(map);
				List<Map<String, Object>> processTopDls = this.processTopDls(topDls);
				returnmap.put("topdls",processTopDls);
				returnmap.put("topdlsnew",topDls);
				return returnmap;
			}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"qd".equals(map.get("flag"))){
				//1.1、渠道收费免费占比
				List<Map<String, Object>> channelChargeSfMfList = dao.getSfMfChannelChargeData(map);
				returnmap.put("channelChargeSfMfList", channelChargeSfMfList);
			}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"qf".equals(map.get("flag"))){
				//1.2、业务类型收费免费占比
				List<Map<String, Object>> businessTypeChargeSfMfList = dao.getSfMfBusinessTypeChargeData(map);
				returnmap.put("businessTypeChargeSfMfList", businessTypeChargeSfMfList);
			}
		}else{
			//营销统计主要统计金额
			//伙伴分类
			List<JcsjDto> hbfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode());
			List<String> hbfls = new ArrayList<>();
			for (JcsjDto jcsjDto : hbfllist) {
				if ("001".equals(jcsjDto.getCsdm()) || "002".equals(jcsjDto.getCsdm()) || "003".equals(jcsjDto.getCsdm()) || "004".equals(jcsjDto.getCsdm())) {
					hbfls.add(jcsjDto.getCsid());
				}
			}
			map.put("hbfls", hbfls);
			//1.1、渠道收费情况
			List<Map<String, Object>> channelChargeList = dao.getSfChannelChargeDataWithAmount(map);
			List<Map<String, Object>> channelChargeNew = new ArrayList<>();
			if (!CollectionUtils.isEmpty(channelChargeList)) {
				List<String> list = new ArrayList<>();
				for (Map<String, Object> channel : channelChargeList) {
					if (!CollectionUtils.isEmpty(channel)) {
						String rq = channel.get("rq").toString();
						Map<String, Object> map_n = new HashMap<>();
						if (!list.contains(rq)) {
							for (Map<String, Object> map_m : channelChargeList) {
								if (rq.equals(map_m.get("rq").toString())) {
									map_n.put(map_m.get("qdmc").toString(), map_m.get("sfnum"));
								}
							}
							map_n.put("rq", rq);
							channelChargeNew.add(map_n);
						}
						list.add(rq);
					}
				}
			}
			returnmap.put("channelChargeList", channelChargeNew);
			//1.2、业务类型收费情况
			List<Map<String, Object>> businessTypeChargeList = dao.getSfBusinessTypeChargeDataWithAmount(map);
			List<Map<String, Object>> businessTypeNew = new ArrayList<>();
			if (!CollectionUtils.isEmpty(businessTypeChargeList)) {
				List<String> list = new ArrayList<>();
				for (Map<String, Object> businessTypeCharge : businessTypeChargeList) {
					if (!CollectionUtils.isEmpty(businessTypeCharge)) {
						String rq = businessTypeCharge.get("rq").toString();
						if (!list.contains(rq)) {
							Map<String, Object> map_n = new HashMap<>();
							for (Map<String, Object> map_m : businessTypeChargeList) {
								if (rq.equals(map_m.get("rq").toString())) {
									map_n.put(map_m.get("qfmc").toString(), map_m.get("sfnum"));
								}
							}
							map_n.put("rq", rq);
							businessTypeNew.add(map_n);
						}
						list.add(rq);
					}
				}
			}
			returnmap.put("businessTypeChargeList", businessTypeNew);
			//1.5、直销top10排行
			returnmap.put("topzx",dao.getZxWithAmount(map));
			//1.11 第三方实验室TOP10排行
			returnmap.put("topdsf",dao.getTopDsfWithAmount(map));
			//1.12 增加代理商(代理+CSO)top10统计图
			List<Map<String, Object>> topDls = dao.getTopDlsWithAmount(map);
			List<Map<String, Object>> processTopDls = this.processTopDls(topDls);
			returnmap.put("topdls",processTopDls);
			returnmap.put("topdlsnew",topDls);
			//1.6、直销核心医院占比
			returnmap.put("zxzdyyList", dao.getCentralHospitalSaleData(map));
			//1.7、医院送检排行
			List<String> hbfls_t = new ArrayList<>();
			for (JcsjDto jcsjDto : hbfllist) {
				if ("002".equals(jcsjDto.getCsdm()) || "003".equals(jcsjDto.getCsdm()) || "004".equals(jcsjDto.getCsdm())) {
					hbfls_t.add(jcsjDto.getCsid());
				}
			}
            map.put("hbfls", hbfls_t);
			List<Map<String, Object>> yyxxphlist = dao.getHospitalSalesNum(map);
			List<Map<String, Object>> yyxxphlist_t = new ArrayList<>();
			if (!CollectionUtils.isEmpty(yyxxphlist)) {
				for (Map<String, Object> hospital : yyxxphlist) {
					Map<String, Object> returnMap = new HashMap<>();
					returnMap.put("dwmc", hospital.get("dwmc"));
					returnMap.put("zh", hospital.get("zh"));
					returnMap.put("yyzddj", hospital.get("yyzddj"));
					returnMap.put("zxsfnum", hospital.get("zxsfnum"));
					returnMap.put("dlssfnum", hospital.get("dlssfnum"));
					returnMap.put("dqxx", hospital.get("dqxx"));
					yyxxphlist_t.add(returnMap);
				}
			}
			returnmap.put("yyxxphlist", yyxxphlist_t);
			//1.8、核心医院送检排行(直销+代理)
			String[] yyzddjs={"A","B","C","A+"};
			map.put("yyzddjs",yyzddjs);
			List<Map<String, Object>> hxyyxxphlist = dao.getHospitalSalesNum(map);
			List<Map<String, Object>> hxyyxxphlist_t =new ArrayList<>();
			if (!CollectionUtils.isEmpty(hxyyxxphlist)){
				for (Map<String, Object> hospital : hxyyxxphlist) {
					Map<String, Object> returnMap_t = new HashMap<>();
					returnMap_t.put("dwmc",hospital.get("dwmc"));
					returnMap_t.put("zxsfnum",hospital.get("zxsfnum"));
					returnMap_t.put("dlssfnum",hospital.get("dlssfnum"));
					returnMap_t.put("yyzddj",hospital.get("yyzddj"));
					returnMap_t.put("zh",hospital.get("zh"));
					returnMap_t.put("dqxx",hospital.get("dqxx"));
					hxyyxxphlist_t.add(returnMap_t);
				}
			}
			returnmap.put("hxyyxxphlist",hxyyxxphlist_t);
			//1.9、入院医院送检排行
			List<Map<String,Object>> ryyySfcssList = dao.getRyyySfcssSaleTopList(map);
			returnmap.put("ryyysfcssList",ryyySfcssList);

			//2.3、月人均收费销售额（公式=累计月人均收费销售额（直销当中的特检、入院、科技）/ 过去月份）
			List<Map<String,Object>> ndyrjSfxseList = dao.getNdyrjSfxseList(map);
			returnmap.put("ndyrjsfxseList",ndyrjSfxseList);
			//2.3.1、特检月人均收费销售额（公式=累计月人均收费销售额（直销当中的特检）/ 过去月份）
			List<Map<String,Object>> tjndyrjsfxseList = dao.getTjNdyrjSfxseList(map);
			returnmap.put("tjndyrjsfxseList",tjndyrjsfxseList);
			//2.3.2、入院月人均收费销售额（公式=累计月人均收费销售额（直销当中的入院）/ 过去月份）
			List<Map<String,Object>> ryndyrjsfxseList = dao.getRyNdyrjSfxseList(map);
			returnmap.put("ryndyrjsfxseList",ryndyrjsfxseList);
		}
		return returnmap;
	}

	private List<Map<String, Object>> processTopDls(List<Map<String, Object>> topDls) {
		List<Map<String, Object>> processTopDls = new ArrayList<>();
		if (!CollectionUtils.isEmpty(topDls)) {
			List<String> list = new ArrayList<>();
			for (Map<String, Object> dls : topDls) {
				if (!CollectionUtils.isEmpty(dls)) {
					String swmc = String.valueOf(dls.get("swmc"));
					String zt = String.valueOf(dls.get("zt"));
					if (!list.contains(swmc)) {
						Map<String, Object> map_n = new HashMap<>();
						BigDecimal total = new BigDecimal("0");
						for (Map<String, Object> map_m : topDls) {
							if (swmc.equals(String.valueOf(map_m.get("swmc")))&&zt.equals(String.valueOf(map_m.get("zt")))) {
								map_n.put("swmc", swmc);
								total = total.add(new BigDecimal(String.valueOf(map_m.get("total"))));
								map_n.put("total", total.toString());
								map_n.put("dqxx",dls.get("dqxx"));
								map_n.put("zt",dls.get("zt"));
							}
						}
						processTopDls.add(map_n);
					}
					list.add(swmc);
				}
			}
			processTopDls.sort(new Comparator<>() {
                                   @Override
                                   public int compare(Map<String, Object> o1, Map<String, Object> o2) {
                                       return new BigDecimal(String.valueOf(o2.get("total"))).subtract(new BigDecimal(String.valueOf(o1.get("total")))).intValueExact();
                                   }
                               }
			);
		}
		return processTopDls;
	}

	/**
	 * 获取质控营销统计数据
	 * @param map
	 * @return
	 */
	public Map<String,Object> getMarketStstictisDataLately(Map<String,Object> map){
		Map<String,Object> returnmap = new HashMap<>();
		if(StringUtil.isBlank(map.get("moneyflag").toString())){
			//营销统计主要统计数目
			//伙伴分类
			List<JcsjDto> hbfllist = redisUtil.lgetDto("List_matridx_jcsj:" + BasicDataTypeEnum.CLASSIFY.getCode());
			//2.1、丢失客户(第三方+代理)
			List<String> hbfls_d = new ArrayList<>();
			for (JcsjDto jcsjDto : hbfllist) {
				if (("001".equals(jcsjDto.getCsdm())||"003".equals(jcsjDto.getCsdm()))){
					hbfls_d.add(jcsjDto.getCsid());
				}
			}
            map.put("hbfls",hbfls_d);
			List<Map<String, Object>> losepartnerlist = dao.getLosePartners(map);
			List<Map<String, Object>> losepartnerlist_d =new ArrayList<>();
			if (!CollectionUtils.isEmpty(losepartnerlist)){
				for (Map<String, Object> losepartner : losepartnerlist) {
					Map<String, Object> returnMap_d = new HashMap<>();
					returnMap_d.put("db",losepartner.get("db"));
					returnMap_d.put("jsrq",losepartner.get("jsrq"));
					returnMap_d.put("dqxx",losepartner.get("dqxx"));
					returnMap_d.put("yellow",losepartner.get("yellow"));
					returnMap_d.put("red",losepartner.get("red"));
					returnMap_d.put("black",losepartner.get("black"));
					returnMap_d.put("zt",losepartner.get("zt"));
					losepartnerlist_d.add(returnMap_d);
				}
			}
			returnmap.put("losepartnerlist",losepartnerlist_d);
			//2.2、区域收费占比情况(直销+代理)
			List<Map<String,Object>> qySfcssList = dao.getQySfcssList(map);
			returnmap.put("qysfcssList",qySfcssList);
		}
//		else{
//			//营销统计主要统计金额
//
//		}
		return returnmap;
	}

	/**
	 * 获取达成率
	 * @param map
	 * @return
	 */
	public List<Map<String, Object>> getKpiList(Map<String,Object> map){
		List<Map<String, Object>> kpiList = new ArrayList<>();
		Map<String, Object> sqlMap = new HashMap<>();
		String zblx;
//		if ("year".equals((String)map.get("type"))){
//			zblx = "Y";
//		}else
		if ("quarter".equals((String)map.get("type"))){
			zblx = "Q";
			Object nfo = map.get("nf");
			Object yfo = map.get("yf");
			boolean isHasMonth = true;
			List<String> zbrqs = new ArrayList<>();
			if (nfo != null && ((List<String>)nfo).size() > 0){
				List<String> nfs = (List<String>)nfo;
				for (String nf : nfs) {
                    if (yfo != null && ((List<String>)yfo).size() > 0){
						List<String> yfs = (List<String>)yfo;
						for (String yf : yfs) {
							zbrqs.add(nf+"-"+yf);
						}
					}else {
						isHasMonth = false;
						zbrqs.add(nf);
					}
				}
				sqlMap.put(isHasMonth?"Mzbrqs":"Yzbrqs",zbrqs);
				sqlMap.put("zblx",zblx);
				sqlMap.put("yhid",map.get("yhid"));
				sqlMap.put("qys",map.get("qys"));
				sqlMap.put("jcxm",map.get("jcxm"));
				kpiList = dao.getKpiList(sqlMap);
			}
		}
//		else if ("month".equals((String)map.get("type"))){
//			zblx = "M";
//		}
		return kpiList;
	}
	/**
	 * 获取质控营销统计数据
	 * @param map
	 * @return
	 */
	public Map<String,Object> getMarketStatisticsDataWithSj(Map<String,Object> map){
		Map<String, Object> returnmap = new HashMap<>();
		if(StringUtil.isBlank(map.get("flag").toString())) {
			List<Map<String, Object>> sampleTypeList = qualityControlOperationsStaticDao.getSampleTypeMakeUp(map);
			returnmap.put("sampleTypeList", sampleTypeList);
			List<Map<String, Object>> inspectionTypeList = qualityControlOperationsStaticDao.getInspectionTypeMakeUp(map);
			returnmap.put("inspectionTypeList", inspectionTypeList);
			List<Map<String, Object>> departmentTypeList = qualityControlOperationsStaticDao.getDepartmentTypeMakeUp(map);
			returnmap.put("departmentTypeList", departmentTypeList);
			List<Map<String, Object>> hospitalTypeList = qualityControlOperationsStaticDao.getHospitalTypeMakeUp(map);
			returnmap.put("hospitalTypeList", hospitalTypeList);
			List<Map<String, Object>> vipTypeList = qualityControlOperationsStaticDao.getVIPTypeMakeUp(map);
			returnmap.put("vipTypeList", vipTypeList);
			List<Map<String, Object>> instrumentTypeList = getInstrument();
			returnmap.put("instrumentTypeList", instrumentTypeList);
			List<Map<String, Object>> SpeTimeSamples = qualityControlOperationsStaticDao.getSpeTimeSamples();
			Map<String, Object> SpeTimeSamples_t=new HashMap<>();
			if (SpeTimeSamples!=null&&SpeTimeSamples.size()>0) {
				for (int i=0;i<SpeTimeSamples.size();i++){
					if (!(SpeTimeSamples.get(i).get("ybsl").toString()).matches("-1")){
						SpeTimeSamples_t.put("ybsl",SpeTimeSamples.get(i).get("ybsl"));
					}
					if (!(SpeTimeSamples.get(i).get("fjsl").toString()).matches("-1")){
						SpeTimeSamples_t.put("fjsl",SpeTimeSamples.get(i).get("fjsl"));
					}
					if (!(SpeTimeSamples.get(i).get("jcsl").toString()).matches("-1")){
						SpeTimeSamples_t.put("jcsl",SpeTimeSamples.get(i).get("jcsl"));
					}
				}
				returnmap.put("SpeTimeSamples", SpeTimeSamples_t);
			}
			List<Map<String,Object>> saturationJcdws = qualityControlOperationsStaticDao.getSaturationJcdw(map);//各个实验室饱和度，返回多行，行信息包括实验室名称，实验室ID，饱和度
			returnmap.put("saturationJcdws", saturationJcdws);
			List<Map<String,Object>> getSaturationAllJcdw = qualityControlOperationsStaticDao.getSaturationAllJcdw(map);//所有实验室的饱和度，返回一个值，即实验室汇总的饱和值
			returnmap.put("getSaturationAllJcdw", getSaturationAllJcdw);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"sample".equals(map.get("flag"))){//送检标本类型占比，返回样本类型名称，数量和百分比
			List<Map<String, Object>> sampleTypeList = qualityControlOperationsStaticDao.getSampleTypeMakeUp(map);
			returnmap.put("sampleTypeList", sampleTypeList);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"inspection".equals(map.get("flag"))){//送检项目占比，送检项目，数量和百分比
			List<Map<String, Object>> inspectionTypeList = qualityControlOperationsStaticDao.getInspectionTypeMakeUp(map);
			returnmap.put("inspectionTypeList", inspectionTypeList);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"department".equals(map.get("flag"))){//科室分布，科室名称，数量和百分比
			List<Map<String, Object>> departmentTypeList = qualityControlOperationsStaticDao.getDepartmentTypeMakeUp(map);
			returnmap.put("departmentTypeList", departmentTypeList);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"hospital".equals(map.get("flag"))){//送检医院分布，医院名称，数量和百分比
			List<Map<String, Object>> hospitalTypeList = qualityControlOperationsStaticDao.getHospitalTypeMakeUp(map);
			returnmap.put("hospitalTypeList", hospitalTypeList);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"vip".equals(map.get("flag"))){//VIP标本占比，是否是VIP，数量和百分比
			List<Map<String, Object>> vipTypeList = qualityControlOperationsStaticDao.getVIPTypeMakeUp(map);
			returnmap.put("vipTypeList", vipTypeList);
		}
		else if (StringUtil.isNotBlank(map.get("flag").toString())&&"instrument".equals(map.get("flag"))){//实验室测序仪数量
			List<Map<String, Object>> instrumentTypeList = getInstrument();
			returnmap.put("instrumentTypeList", instrumentTypeList);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"timeout".equals(map.get("flag"))){//运送TAT，报告超时数量，运送超时数量
			List<Map<String, Object>> timeoutNumber = qualityControlOperationsStaticDao.getTimeoutNumber();
			returnmap.put("timeoutNumber", timeoutNumber);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"timeoutbyjcdw".equals(map.get("flag"))){//运送TAT按照实验室，报告超时数量，运送超时数量，实验室
			List<Map<String, Object>> timeoutNumberGroupByJcdw = qualityControlOperationsStaticDao.getTimeoutNumberGroupByJcdw();
			returnmap.put("timeoutNumberGroupByJcdw", timeoutNumberGroupByJcdw);
		}else if (StringUtil.isNotBlank(map.get("flag").toString())&&"dailyybs".equals(map.get("flag"))){//每日样本数
			List<Map<String, Object>> dailyybslist = qualityControlOperationsStaticDao.getDailyybslist();
			returnmap.put("dailyybslist", dailyybslist);
		}
		return returnmap;
	}

	/**
	 * 获取平台销售指标达成率
	 * @param xszbDto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getPtzbdcl(XszbDto xszbDto) {
		//去除ptmc为null的情况，该情况是不存在指标导致的
		List<Map<String, Object>> ptzbdclList = dao.getPtzbdcl(xszbDto);
		List<Map<String, Object>> res = new ArrayList<>();
		for (Map<String,Object> map:ptzbdclList){
			if (StringUtil.isNotBlank( (String)map.get("ptmc")) ){
				res.add(map);
			}
		}
		return res;
	}
	/**
	 * 获取业务发展部
	 * @param xszbDto
	 * @return
	 */
	@Override
	public List<Map<String, Object>> getYwfzbZbdcl(XszbDto xszbDto) {
		return dao.getYwfzbZbdcl(xszbDto);
	}

	/**
	 * 获取实验室对应的信息
	 * @return
	 */
	List<Map<String, Object>> getInstrument(){
		List<Map<String, Object>> mapList = new ArrayList<>();
		List<JcsjDto> sequencer = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.SEQUENCER_CODE.getCode());
		List<JcsjDto> jcdwlist = redisUtil.hmgetDto("matridx_jcsj:" + BasicDataTypeEnum.DETECTION_UNIT.getCode());
		for (int i = 0; i < jcdwlist.size(); i++) {
			String fcsid=jcdwlist.get(i).getCsid();
			Map<String, Object> result=new HashMap<>();
			int count=0;
			for (int j = 0; j < sequencer.size(); j++) {
				if(	fcsid.equals(sequencer.get(j).getFcsid())){
					count++;
				}
			}
			result.put("num",String.valueOf(count));//map的cxy存放着测序仪的数量，方便日后扩展提取仪等
			result.put("sys",jcdwlist.get(i).getCsmc());//map的cxy存放着测序仪的数量，方便日后扩展提取仪等
			mapList.add(result);
		}
		return  mapList;
	}

	/**
	 * 特检销售各区域平台达成率
	 * @param xszbDto
	 * @return
	 */
	@Override
	public List<Map<String,String>> getTjsxdcl(XszbDto xszbDto) {
		return dao.getTjsxdcl(xszbDto);
	}

	/**
	 * 获取伙伴分类测试数统计
	 */
	public List<Map<String, String>> getHbflTestCountStatistics(SjxxDto sjxxDto){
		return dao.getHbflTestCountStatistics(sjxxDto);
	}

	/**
	 * 获取合作伙伴统计
	 */
	public List<Map<String, String>> getStatisticsByTjFlAndJsrq(SjxxDto sjxxDto){return dao.getStatisticsByTjFlAndJsrq(sjxxDto);}

	/**
	 * 获取合作伙伴测试数占比
	 */
	public List<Map<String, String>> getHbflcsszb(SjxxDto sjxxDto){
		return dao.getHbflcsszb(sjxxDto);
	}
	/**
	 * 获取送检区分测试数占比
	 */
	public List<Map<String, String>> getSjqfcsszb(SjxxDto sjxxDto){
		return dao.getSjqfcsszb(sjxxDto);
	}
}
