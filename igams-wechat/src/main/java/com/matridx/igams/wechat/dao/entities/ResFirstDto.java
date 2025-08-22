package com.matridx.igams.wechat.dao.entities;

import com.matridx.igams.common.dao.entities.BaseBasicModel;
import com.matridx.springboot.util.base.StringUtil;
import com.taobao.api.internal.util.StringUtils;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResFirstDto extends BaseBasicModel {
    /**
	 * 
	 */
	private static final long serialVersionUID = 6824884288493205467L;
	//送检ID
    private String sjid;
    //患者姓名
    private String hzxm;
    //性别
    private String xb;
    //年龄
    private String nl;
    //电话
    private String dh;
    //送检单位
    private String sjdw;
    //科室
    private String ks;
    //送检医生  暂时不关联
    private String sjys;
    //医生电话
    private String ysdh;
    //代表  关联微信用户
    private String db;
    //标本类型  关联基础数据
    private String yblx;
    //标本类型名称 标本类型为其他的时候填写
    private String yblxmc;
    //标本体积 任意填写
    private String ybtj;
    //住院号
    private String zyh;
    //标本编号
    private String ybbh;
    //采样日期
    private String cyrq;
    //接收日期
    private String jsrq;
    //接收人员
    private String jsry;
    //报告日期
    private String bgrq;
    //临床症状
    private String lczz;
    //前期诊断
    private String qqzd;
    //近期用药
    private String jqyy;
    //前期检测
    private String qqjc;
    //送检报告
    private String sjbg;
    //检验结果  0：未检验 1：已检验  2：检验通过 3：检验未通过
    private String jyjg;
    //快递类型
    private String kdlx;
    //快递单号
    private String kdh;
    //快递费用
    private String kdfy;
    //备注
    private String bz;
    //状态
    private String zt;
    //付款标记
    private String fkbj;
    //付款金额
    private String fkje;
    //实付金额
    private String sfje;
    //内部编号
    private String nbbm;
    //年龄单位
    private String nldw;
    //其他科室
    private String qtks;
    //检测标记
    private String jcbj;
    //检测标记名称
    private String jcbjmc;
    //是否收费
    private String sfsf;
    //实验日期
    private String syrq;
    //DNA检测标记
    private String djcbj;
    //DNA实验日期
    private String dsyrq;
    //是否上传结果
    private String sfsc;
    //床位号
    private String cwh;
    //外部编码
    private String wbbm;
    //是否自免检测  0：否  1：是
    private String sfzmjc;
    //自免项目
    private String zmxm;
    //自免目的地
    private String zmmdd;
    //其他检测标记
    private String qtjcbj;
    //其他实验日期
    private String qtsyrq;
    //送检区分
    private String sjqf;
    //是否开票
    private String kpsq;
    //送检单位名称
    private String sjdwmc;
    //检测子项目
    private String jczxm;
    //退款方式
    private String tkfs;
    //退款金额
    private String tkje;
    //顺丰标记（已签收订单为1，订单号查找不到为2）
    private String sfbj;
    //检测结果(json存病原体列表的pathpgen的中文名称)
    private String jcjg;
    //疑似结果(json存background的中文名称)
    private String ysjg;
    //镁信id
    private String mxid;
    //是否VIP
    private String sfvip;
    //科研项目
    private String kyxm;
    //参数扩展6
    private String cskz6;
    //医生ID
    private String ysid;
    //微信ID
    private String wxid;
    //送检临床症状
    private List<String> lczzs;
    //检测项目ID复数
    private List<String> jcxmids;
    //送检临床症状
    private List<SjlczzDto> sjlczzs;
    //检测项目ID复数
    private List<SjjcxmDto> sjjcxms;
    //前期检测复数
    private List<SjqqjcDto> sjqqjcs;
    //前期关注病原复数
    private List<SjgzbyDto> sjgzbys;
    //送检标本状态复数
    private List<SjybztDto> sjybzts;
    //关注病原复数
    private List<String> bys;
    //扫码验证用参数
    private String code;
    //扫码验证用参数
    private String state;
    //代表名
    private String dbm;
    //起始页 对外接口用
    private String startpage;
    //申请起始日 对外接口用
    private String applydatestart;
    //标本编号 对外接口用
    private String samplecode;
    //送检单位
    private String dwmc;
    //标本类型
    private String csmc;
    //开始日期
    private String jsrqstart;
    //结束日期
    private String jsrqend;
    //开始日期(月查询)
    private String jsrqMstart;
    //结束日期(月查询)
    private String jsrqMend;
    //开始日期(年查询)
    private String jsrqYstart;
    //结束日期(年查询)
    private String jsrqYend;
    //文件类型
    private String ywlx;
    //文件类型(word)
    private String w_ywlx;
    //科研项目（多）
    private String[] kyxms;
    //科研项目类型
    private String kylx;
    //科研项目类型（多）
    private String[] kylxs;
    //科研项目名称
    private String kyxmmc;
    //文件类型 去人源
    private String ywlx_q;
    //附件ID
    private String fjid;
    //Word版附件ID
    private String wordfjid;
    //Word版文件名
    private String wordwjm;
    //附件ID复数
    private List<String> fjids;
    //附件ID复数 去人源
    private List<String> fjids_q;
    //检索日期范围复数
    private List<String> rqs;
    //已检项目
    private String yjxmmc;
    //性別名称
    private String xbmc;
    //科室名称
    private String ksmc;
    //检测项目名称
    private String jcxmmc;
    //检测项目扩展参数
    private String jcxmcskz;
    //检测项目扩展参数二
    private String jcxmcskz2;
    //检测项目扩展参数三
    private String jcxmcskz3;
    //检测项目参数代码
    private String jcxmdm;
    //关注病原名称
    private String gzbymc;
    //前期检测名称
    private String qqjcmc;
    //临床症状名称
    private String lczzmc;
    //标本状态名称
    private String ybztmc;
    //用于检索科室
    private String[] dwids;
    //用于检索标本类型
    private String[] yblxs;
    //是否付款
    private String fkmc;
    //销售人员
    private String zsxm;
    //导出关联标记位
    //所选择的字段
    private String SqlParam;
    //标本类型
    private String yblx_flg;
    //送检单位
    private String sjdwxx_flg;
    //付款日期
    private String fkrq;
    //钉钉ID
    private String ddid;
    //文件路径
    private String wjlj;
    //文件名
    private String wjm;
    //关联用户的微信ID
    private String wechatid;
    //合作伙伴的伙伴类型
    private String hblx;
    //高级修改
    private String xg_flg;
    //返回状态
    private String status;
    //模板代码
    private String mbdm;
    //序号
    private int xh;
    //发送方式
    private String fsfs;
    //邮箱
    private String yx;
    //查询条件
    private String tj;
    //开始日期
    private String bgrqstart;
    //结束日期
    private String bgrqend;
    //开始日期(月查询)
    private String bgrqMstart;
    //结束日期(月查询)
    private String bgrqMend;
    //开始日期(年查询)
    private String bgrqYstart;
    //结束日期(年查询)
    private String bgrqYend;
    //是否收样 0为否  1为是
    private String sfsy;
    //是否接收
    private String sfjs;
    //是否统计
    private String sftj;
    //标本状态
    private List<String> zts;
    //判断是否修改接收日期
    private String jstj;
    //合作伙伴参数扩展2
    private String hbcskz2;
    //合作伙伴参数扩展3
    private String hbcskz3;
    //合作伙伴集合
    private List<String> sjhbs;
    //签名验证
    private String sign;
    //参数扩展二
    private String cskz2;
    //参数扩展三
    private String cskz3;
    //参数扩展四
    private String cskz4;
    //参数扩展五
    private String cskz5;
    //用于检索送检扩展参数1
    private String[] sjkzcs1;
    //用于检索送检扩展参数2
    private String[] sjkzcs2;
    //用于检索送检扩展参数3
    private String[] sjkzcs3;
    //用于检索送检扩展参数4
    private String[] sjkzcs4;
    //检测项目（用于导出）
    private String jcxm;
    //排序
    private String px;
    //接收日期格式化
    private String fjsrq;
    //标本编号list
    private List<String> ybbhs;
    //是否接收名称
    private String sfjsmc;
    //是否统计名称
    private String sftjmc;
    //是否收费名称
    private String sfsfmc;
    //是否收费
    private String[] sfsfs;
    //上机时间
    private String sjsj;
    //查询条件(全部)
    private String entire;
    //检测结果名称
    private String[] jcjgmc;

    //伙伴分类
    private String hbfl;
    //伙伴子分类
    private String hbzfl;
    //周期
    private String zq;
    //代表复数
    private List<String> dbs;
    //代表名称复数，用于group 后的结果
    private List<String> dbmcs;
    //伙伴分类复数
    private List<String> hbfls;
    //是否收费标记
    private String sfflg;
    //参数扩展1标记
    private String cskz1_flg;
    //参数扩展2标记
    private String cskz2_flg;
    //参数扩展3标记
    private String cskz3_flg;
    //参数扩展4标记
    private String cskz4_flg;
    //复检原因
    private String yy;
    //复检类型
    private String lx;
    //检测项目扩展参数
    private String jcxmkzcs;
    //录入时间开始时间
    private String lrsjStart;
    //录入时间结束时间
    private String lrsjEnd;
    //用户id
    private String yhid;
    //报告标记
    private String bgbj;
    //检测类型
    private String jclx;
    //pdf报告类型
    private String pdflx;
    //word报告类型
    private String wordlx;
    //收费标准
    private String sfbz;
    //是否正确
    private String sfzq;
    //临床反馈
    private String lcfk;
    //自定义统计参数集
    private Map<String, String> param;
    //统计字段数组 主字段
    private String[] tjzds;
    //统计字段数组 主表的字段
    private String[] mainSelects;
    //统计字段数组 主表group的字段
    private String[] mainGroups;
    //group条件 主要是去除检索字段里的AS信息，用于统合
    private String[] groupzds;
    //检索字段 主要是增加AS信息，用于显示名称
    private String[] selectzds;
    //销售分类
    private String fl;
    //销售分类
    private String zfl;
    //报告模板
    private String bgmb;
    //报告模板名称
    private String bgmbmc;
    //检验结果
    private String[] jyjgs;
    //个数
    private String cn;
    //分类名称
    private String flmc;
    //子分类名称
    private String zflmc;
    //检测项目id
    private String jcxmid;
    //统计周期
    private String tjzq;
    //是否正确名称
    private String sfzqmc;
    //盖章类型
    private String gzlx;
    //盖章类型名称
    private String gzlxmc;
    //流水号
    private String serial;
    //检测项目代码
    private String jcdm;
    //标本类型代码
    private String lxdm;
    //调用打印机标记
    private String print_flg;
    //标本状态标记
    private String ybzt_flg;
    //标本状态参数扩展2（确定发送的消息内容）
    private String ybzt_cskz2;
    //钉钉小程序传递查询关键字
    private String cxnr;
    //查询数
    private String count;
    //开始位置
    private String start;
    //文件名(复数)
    private List<String> wjms;
    //起运日期
    private String qyrq;
    //运达日期
    private String ydrq;
    //预计运达日期
    private String yjydrq;
    //快递类型名称
    private String kdlxmc;
    //起运时间开始
    private String qyrqstart;
    //起运时间结束
    private String qyrqend;
    //运达时间开始
    private String ydrqstart;
    //运达时间结束
    private String ydrqend;
    //用于检索快递类型
    private String[] kdlxs;
    //用于检索盖章类型
    private String[] gzlxs;
    //打印份数
    private String print_num;
    //检测单位
    private String jcdw;
    //检测单位（多）
    private String[] jcdws;
    //检测单位名称
    private String jcdwmc;
    //转管打印机ip
    private String print_demise_ip;
    //微信ID(复数)
    private List<String> wxids;
    //采样日期(开始)
    private String cyrqstart;
    //采样日期(结束)
    private String cyrqend;
    //单位限制标记，0：不限制单位，1：限制单位
    private String dwxzbj;
    //录入人员(复数)
    private List<String> lrrys;
    //物种分类
    private String wzfl;
    //物种分类类型
    private String  wzfllx;
    //角色id
    private String jsid;
    //角色检测单位限制
    private List<String> jcdwxz;
    //报告类型标记(pdf,word)
    private String bglxbj;
    //接收人员名称
    private String jsrymc;
    //错误ID
    private String cwid;
    //分文件路径
    private String fwjlj;
    //分文件名
    private String fwjm;
    //是否自免格式化
    private String sfzmmc;
    //自免项目名称
    private String zmxmmc;
    //自免目的地名称
    private String zmmddmc;
    //last外部编码
    private String lastwbbm;
    //标题
    private String bt;
    //内容
    private String nr;
    //之前的付款金额
    private String old_fkje;
    //实验日期开始
    private String syrqstart;
    //实验日期结束
    private String syrqend;
    //实验日期标记 高级检索用
    private String syrqflg;
    //检测项目[多]
    private String[] jcxms;
    //送检区分[多]
    private String[] sjqfs;
    //合作伙伴分类[多]
    private String[] sjhbfls;
    //合作伙伴子分类[多]
    private String[] sjhbzfls;
    //报告发送名称
    private String bgfsmc;
    //报告发送代码
    private String bgfsdm;
    //是否发送(0:不发送, 1:发送)
    private String sffs;
    //检测单位简称
    private String jcdwjc;
    //基础数据扩展参数2
    private String jc_cskz2;
    //基础数据扩展参数3
    private String jc_cskz3;
    //标本类型代码
    private String yblxdm;
    //标本状态拓展参数
    private String ybztcskz1;
    //医院名称(送检单位名称)
    private String hospitalname;
    //医院名称导出标记
    private String hospitalName_flg;
    //医院信息扩展参数1
    private String yyxxCskz1;
    //医院信息扩展参数3
    private String yyxxCskz3;
    //送检区分名称
    private String sjqfmc;
    //是否开票申请
    private String kpsqmc;
    //送检单位简称
    private String dwjc;
    //医院名称
    private String yymc;
    //复检类型[多]
    private List<String> fjlxs;
    //送检验证id
    private String yzid;
    //模糊查询全部参数
    private String all_param;
    //判断是否是个人清单 single_flag=1 为个人清单  single_flag=0 or single_flag=null 为全部清单
    private String single_flag;
    //用于个人清单判断lrry [yhid,ddid,wxid]
    private List<String> userids;
    //类型名称
    private String lxmc;
    //是否实验通知
    private String sfsytz;
    //附件保存标记  local:代表本地。否则为从钉钉
    private String fjbcbj;
    //区分名称字段，主要是区分正常送检还是复检，其中复检包含几种类型
    private String qf;
    //区分标记，正常送检：0，复检：1
    private String flg_qf;
    //用户名
    private String yhm;
    //付款日期开始时间
    private String fkrqstart;
    //付款日期结束时间
    private String fkrqend;
    //检测子项目名称
    private String jczxmmc;
    // 伙伴分类(sql拆分使用)
    private String hb_fl;
    // 伙伴用户ID(sql拆分使用)
    private String hb_yhid;
    // 伙伴ID(sql拆分使用)
    private String hbid;
    //不接收数
    private String bjss;
    //收费数
    private String sfs;
    //不收费数
    private String bsfs;
    //总数
    private String zs;
    //查询快递号每次查询最多十个
    private String[] kdhs;
    //有数据的合作伙伴
    private String dbhbs;
    //时效性
    private String sxx;
    //复测率数据
    private String fclsj;
    //复测率
    private String fcl;
    //代表岗位-复数
    private List<String> dbgws;
    //人数
    private String rs;
    //关注病院s
    private List<String> ygzbys;
    private String yzlb;
    private String yzjg;
    private String yzjgmc;
    private String qfmc;
    private String khtz;
    private String sjph;
    private String ywbh;
    private String jl;
    private String fzhxjg;
    //参数id
    private String csid;
    //文件
    private String wjmgz;
    //人源指数
    private String ryzs;
    //人源指数百分比
    private String ryzsbfb;
    //GC含量
    private String gc;
    //总序列数
    private String zxls;
    //宿主序列百分比
    private String szxlbfb;
    //有效序列数
    private String yxxls;
    //阈值
    private String spikerpm;
    //申请类型
    private String sqlxmc;
    //科室扩展参数2
    private String kskzcs2;
    //检测类型s
    private List<String> jclxs;
    //实付总金额
    private String sfzje;
    //退款总金额
    private String tkzje;
    //送检id
    private String sjpdid;
    //医院重点等级
    private String yyzddj;
    //医院重点等级（复数）
    private String[] yyzddjs;
    //医院统计名称
    private String yytjmc;
    //送检日期
    private String sjrq;
    //荧光值
    private String intensity;
    //簇密度
    private String density;
    public String getSjpdid() {
        return sjpdid;
    }

    public void setSjpdid(String sjpdid) {
        this.sjpdid = sjpdid;
    }

    public String getIntensity() {
        return intensity;
    }

    public void setIntensity(String intensity) {
        this.intensity = intensity;
    }

    public String getDensity() {
        return density;
    }

    public void setDensity(String density) {
        this.density = density;
    }

    public String getSjrq() {
        return sjrq;
    }

    public void setSjrq(String sjrq) {
        this.sjrq = sjrq;
    }

    public String getYyzddj() {
        return yyzddj;
    }

    public void setYyzddj(String yyzddj) {
        this.yyzddj = yyzddj;
    }

    public String[] getYyzddjs() {
        return yyzddjs;
    }

    public void setYyzddjs(String[] yyzddjs) {
        this.yyzddjs = yyzddjs;
        for (int i = 0; i <yyzddjs.length; i++){
            this.yyzddjs[i]=this.yyzddjs[i].replace("'","");
        }
    }

    public String getYytjmc() {
        return yytjmc;
    }

    public void setYytjmc(String yytjmc) {
        this.yytjmc = yytjmc;
    }

    public String getTkzje() {
        return tkzje;
    }

    public void setTkzje(String tkzje) {
        this.tkzje = tkzje;
    }

    public String getSfzje() {
        return sfzje;
    }

    public void setSfzje(String sfzje) {
        this.sfzje = sfzje;
    }

    public List<String> getJclxs() {
        return jclxs;
    }
    public void setJclxs(List<String> jclxs) {
        this.jclxs = jclxs;
    }

    public String getKskzcs2() {
        return kskzcs2;
    }

    public void setKskzcs2(String kskzcs2) {
        this.kskzcs2 = kskzcs2;
    }

    public String getSqlxmc() {
        return sqlxmc;
    }

    public void setSqlxmc(String sqlxmc) {
        this.sqlxmc = sqlxmc;
    }

    public String getJcxmdm() {
        return jcxmdm;
    }

    public void setJcxmdm(String jcxmdm) {
        this.jcxmdm = jcxmdm;
    }

    public String getRyzs() {
        return ryzs;
    }

    public void setRyzs(String ryzs) {
        this.ryzs = ryzs;
    }

    public String getRyzsbfb() {
        return ryzsbfb;
    }

    public void setRyzsbfb(String ryzsbfb) {
        this.ryzsbfb = ryzsbfb;
    }

    public String getGc() {
        return gc;
    }

    public void setGc(String gc) {
        this.gc = gc;
    }

    public String getZxls() {
        return zxls;
    }

    public void setZxls(String zxls) {
        this.zxls = zxls;
    }

    public String getSzxlbfb() {
        return szxlbfb;
    }

    public void setSzxlbfb(String szxlbfb) {
        this.szxlbfb = szxlbfb;
    }

    public String getYxxls() {
        return yxxls;
    }

    public void setYxxls(String yxxls) {
        this.yxxls = yxxls;
    }

    public String getSpikerpm() {
        return spikerpm;
    }

    public void setSpikerpm(String spikerpm) {
        this.spikerpm = spikerpm;
    }

    public String getFzhxjg() {
        return fzhxjg;
    }

    public void setFzhxjg(String fzhxjg) {
        this.fzhxjg = fzhxjg;
    }

    public String getHbcskz3() {
        return hbcskz3;
    }

    public void setHbcskz3(String hbcskz3) {
        this.hbcskz3 = hbcskz3;
    }

    public String getJc_cskz3() {
        return jc_cskz3;
    }

    public void setJc_cskz3(String jc_cskz3) {
        this.jc_cskz3 = jc_cskz3;
    }

    public String getJcxmcskz2() {
        return jcxmcskz2;
    }

    public void setJcxmcskz2(String jcxmcskz2) {
        this.jcxmcskz2 = jcxmcskz2;
    }

    public String getJcxmcskz3() {
        return jcxmcskz3;
    }

    public void setJcxmcskz3(String jcxmcskz3) {
        this.jcxmcskz3 = jcxmcskz3;
    }

    public String getCsid() {
        return csid;
    }

    public void setCsid(String csid) {
        this.csid = csid;
    }
    public String getYzlb() {
        return yzlb;
    }

    public void setYzlb(String yzlb) {
        this.yzlb = yzlb;
    }

    public String getKyxmmc() {
        return kyxmmc;
    }

    public void setKyxmmc(String kyxmmc) {
        this.kyxmmc = kyxmmc;
    }
    public String[] getKyxms()
    {
        return kyxms;
    }
    public void setKyxms(String[] kyxms)
    {
        this.kyxms = kyxms;
        for (int i = 0; i <kyxms.length; i++){
            this.kyxms[i]=this.kyxms[i].replace("'","");
        }
    }
    public String[] getKylxs()
    {
        return kylxs;
    }
    public void setKylxs(String[] kylxs)
    {
        this.kylxs = kylxs;
        for (int i = 0; i <kylxs.length; i++){
            this.kylxs[i]=this.kylxs[i].replace("'","");
        }
    }

    public String getYzjg() {
        return yzjg;
    }

    public void setYzjg(String yzjg) {
        this.yzjg = yzjg;
    }

    public String getYzjgmc() {
        return yzjgmc;
    }

    public void setYzjgmc(String yzjgmc) {
        this.yzjgmc = yzjgmc;
    }

    public String getQfmc() {
        return qfmc;
    }

    public void setQfmc(String qfmc) {
        this.qfmc = qfmc;
    }

    public String getKhtz() {
        return khtz;
    }

    public void setKhtz(String khtz) {
        this.khtz = khtz;
    }

    public String getSjph() {
        return sjph;
    }

    public void setSjph(String sjph) {
        this.sjph = sjph;
    }

    public String getYwbh() {
        return ywbh;
    }

    public void setYwbh(String ywbh) {
        this.ywbh = ywbh;
    }

    public String getJl() {
        return jl;
    }
    public void setJl(String jl) {
        this.jl = jl;
    }

    public List<String> getYgzbys() {
        return ygzbys;
    }

    public void setYgzbys(List<String> ygzbys) {
        this.ygzbys = ygzbys;
    }

    public String getRs() {
        return rs;
    }

    public void setRs(String rs) {
        this.rs = rs;
    }

    public String getSxx() {
        return sxx;
    }

    public void setSxx(String sxx) {
        this.sxx = sxx;
    }

    public String getFclsj() {
        return fclsj;
    }

    public void setFclsj(String fclsj) {
        this.fclsj = fclsj;
    }

    public String getFcl() {
        return fcl;
    }

    public void setFcl(String fcl) {
        this.fcl = fcl;
    }

    public String getDbhbs() {
        return dbhbs;
    }

    public void setDbhbs(String dbhbs) {
        this.dbhbs = dbhbs;
    }

    public String[] getKdhs() {
        return kdhs;
    }

    public void setKdhs(String[] kdhs) {
        this.kdhs = kdhs;
    }

    public String getBjss() {
        return bjss;
    }

    public void setBjss(String bjss) {
        this.bjss = bjss;
    }

    public String getSfs() {
        return sfs;
    }

    public void setSfs(String sfs) {
        this.sfs = sfs;
    }

    public String getBsfs() {
        return bsfs;
    }

    public void setBsfs(String bsfs) {
        this.bsfs = bsfs;
    }

    public String getZs() {
        return zs;
    }

    public void setZs(String zs) {
        this.zs = zs;
    }

    public String getHbid() {
        return hbid;
    }

    public void setHbid(String hbid) {
        this.hbid = hbid;
    }

    public String getHb_fl() {
        return hb_fl;
    }

    public void setHb_fl(String hb_fl) {
        this.hb_fl = hb_fl;
    }

    public String getHb_yhid() {
        return hb_yhid;
    }

    public void setHb_yhid(String hb_yhid) {
        this.hb_yhid = hb_yhid;
    }

    public String getYbzt_cskz2() {
        return ybzt_cskz2;
    }

    public void setYbzt_cskz2(String ybzt_cskz2) {
        this.ybzt_cskz2 = ybzt_cskz2;
    }

    public String getJczxmmc() {
        return jczxmmc;
    }

    public void setJczxmmc(String jczxmmc) {
        this.jczxmmc = jczxmmc;
    }

    public String getFkrqstart() {
        return fkrqstart;
    }

    public void setFkrqstart(String fkrqstart) {
        this.fkrqstart = fkrqstart;
    }

    public String getFkrqend() {
        return fkrqend;
    }

    public void setFkrqend(String fkrqend) {
        this.fkrqend = fkrqend;
    }

    public String getYhm() {
        return yhm;
    }

    public void setYhm(String yhm) {
        this.yhm = yhm;
    }

    public String getQf() {
        return qf;
    }

    public void setQf(String qf) {
        this.qf = qf;
    }

    public String getFlg_qf() {
        return flg_qf;
    }

    public void setFlg_qf(String flg_qf) {
        this.flg_qf = flg_qf;
    }
    public String getFjbcbj() {
        return fjbcbj;
    }

    public void setFjbcbj(String fjbcbj) {
        this.fjbcbj = fjbcbj;
    }

    public String getSfsytz() {
        return sfsytz;
    }

    public void setSfsytz(String sfsytz) {
        this.sfsytz = sfsytz;
    }

    public String getLxmc() {
        return lxmc;
    }

    public void setLxmc(String lxmc) {
        this.lxmc = lxmc;
    }

    public String getYwlx_q() {
        return ywlx_q;
    }

    public void setYwlx_q(String ywlx_q) {
        this.ywlx_q = ywlx_q;
    }

    public List<String> getFjids_q() {
        return fjids_q;
    }

    public void setFjids_q(List<String> fjids_q) {
        this.fjids_q = fjids_q;
    }

    public String getYymc() {
        return yymc;
    }

    public void setYymc(String yymc) {
        this.yymc = yymc;
    }

    public String getYbztcskz1() {
        return ybztcskz1;
    }

    public void setYbztcskz1(String ybztcskz1) {
        this.ybztcskz1 = ybztcskz1;
    }
    //伙伴省份
    private String sf;
    //省份名称
    private String sfmc;
    //收费测试数
    private String sfcss;

    public String getSfcss() {
        return sfcss;
    }

    public void setSfcss(String sfcss) {
        this.sfcss = sfcss;
    }

    public String getSfmc() {
        return sfmc;
    }

    public void setSfmc(String sfmc) {
        this.sfmc = sfmc;
    }

    public String getSf() {
        return sf;
    }

    public void setSf(String sf) {
        this.sf = sf;
    }

    public String getYblxdm() {
        return yblxdm;
    }

    public void setYblxdm(String yblxdm) {
        this.yblxdm = yblxdm;
    }

    public String getJcdwjc() {
        return jcdwjc;
    }

    public void setJcdwjc(String jcdwjc) {
        this.jcdwjc = jcdwjc;
    }

    public String getSffs() {
        return sffs;
    }

    public void setSffs(String sffs) {
        this.sffs = sffs;
    }

    public String getBgfsmc() {
        return bgfsmc;
    }

    public void setBgfsmc(String bgfsmc) {
        this.bgfsmc = bgfsmc;
    }

    public String getBgfsdm() {
        return bgfsdm;
    }

    public void setBgfsdm(String bgfsdm) {
        this.bgfsdm = bgfsdm;
    }

    public String[] getSjhbfls() {
        return sjhbfls;
    }

    public void setSjhbfls(String[] sjhbfls) {
        this.sjhbfls = sjhbfls;
        for (int i = 0; i < sjhbfls.length; i++){
            this.sjhbfls[i]=this.sjhbfls[i].replace("'","");
        }
    }

    public String[] getSjhbzfls() {
        return sjhbzfls;
    }

    public void setSjhbzfls(String[] sjhbzfls) {
        this.sjhbzfls = sjhbzfls;
        for (int i = 0; i < sjhbzfls.length; i++){
            this.sjhbzfls[i]=this.sjhbzfls[i].replace("'","");
        }
    }

    public String[] getSjqfs() {
        return sjqfs;
    }

    public void setSjqfs(String[] sjqfs) {
        this.sjqfs = sjqfs;
        for (int i = 0; i < sjqfs.length; i++){
            this.sjqfs[i]=this.sjqfs[i].replace("'","");
        }
    }

    public String getLastwbbm() {
        return lastwbbm;
    }

    public void setLastwbbm(String lastwbbm) {
        this.lastwbbm = lastwbbm;
    }

    public String getFwjlj() {
        return fwjlj;
    }

    public void setFwjlj(String fwjlj) {
        this.fwjlj = fwjlj;
    }

    public String getFwjm() {
        return fwjm;
    }

    public void setFwjm(String fwjm) {
        this.fwjm = fwjm;
    }

    public String getCwid() {
        return cwid;
    }

    public void setCwid(String cwid) {
        this.cwid = cwid;
    }

    public String getBglxbj() {
        return bglxbj;
    }

    public void setBglxbj(String bglxbj) {
        this.bglxbj = bglxbj;
    }

    public List<String> getJcdwxz()
    {
        return jcdwxz;
    }

    public void setJcdwxz(List<String> jcdwxz)
    {
        this.jcdwxz = jcdwxz;
    }

    public String getJsid()
    {
        return jsid;
    }

    public void setJsid(String jsid)
    {
        this.jsid = jsid;
    }

    public String getWzfl()
    {
        return wzfl;
    }

    public void setWzfl(String wzfl)
    {
        this.wzfl = wzfl;
    }

    public String getWzfllx()
    {
        return wzfllx;
    }

    public void setWzfllx(String wzfllx)
    {
        this.wzfllx = wzfllx;
    }

    public List<String> getLrrys() {
        return lrrys;
    }

    public void setLrrys(List<String> lrrys) {
        this.lrrys = lrrys;
    }

    public String getDwxzbj() {
        return dwxzbj;
    }

    public void setDwxzbj(String dwxzbj) {
        this.dwxzbj = dwxzbj;
    }

    public List<String> getWxids() {
        return wxids;
    }

    public void setWxids(List<String> wxids) {
        this.wxids = wxids;
    }

    public String getPrint_demise_ip()
    {
        return print_demise_ip;
    }

    public void setPrint_demise_ip(String print_demise_ip)
    {
        this.print_demise_ip = print_demise_ip;
    }

    public String getJcdwmc()
    {
        return jcdwmc;
    }

    public void setJcdwmc(String jcdwmc)
    {
        this.jcdwmc = jcdwmc;
    }

    public String[] getJcdws()
    {
        return jcdws;
    }

    public void setJcdws(String[] jcdws)
    {
        this.jcdws = jcdws;
        for (int i = 0; i <jcdws.length; i++){
            this.jcdws[i]=this.jcdws[i].replace("'","");
        }
    }

    public String getJcdw()
    {
        return jcdw;
    }

    public void setJcdw(String jcdw)
    {
        this.jcdw = jcdw;
    }

    public String getPrint_num()
    {
        return print_num;
    }

    public void setPrint_num(String print_num)
    {
        this.print_num = print_num;
    }

    public String[] getKdlxs()
    {
        return kdlxs;
    }

    public void setKdlxs(String[] kdlxs)
    {
        this.kdlxs = kdlxs;
        for (int i = 0; i < kdlxs.length; i++){
            this.kdlxs[i]=this.kdlxs[i].replace("'","");
        }
    }

    public String[] getGzlxs() {
        return gzlxs;
    }

    public void setGzlxs(String[] gzlxs) {
        this.gzlxs = gzlxs;
        for (int i = 0; i < gzlxs.length; i++){
            this.gzlxs[i]=this.gzlxs[i].replace("'","");
        }
    }

    public String getQyrqstart()
    {
        return qyrqstart;
    }

    public void setQyrqstart(String qyrqstart)
    {
        this.qyrqstart = qyrqstart;
    }

    public String getQyrqend()
    {
        return qyrqend;
    }

    public void setQyrqend(String qyrqend)
    {
        this.qyrqend = qyrqend;
    }

    public String getYdrqstart()
    {
        return ydrqstart;
    }

    public void setYdrqstart(String ydrqstart)
    {
        this.ydrqstart = ydrqstart;
    }

    public String getYdrqend()
    {
        return ydrqend;
    }

    public void setYdrqend(String ydrqend)
    {
        this.ydrqend = ydrqend;
    }

    public String getKdlxmc()
    {
        return kdlxmc;
    }

    public void setKdlxmc(String kdlxmc)
    {
        this.kdlxmc = kdlxmc;
    }

    public String getYjydrq()
    {
        return yjydrq;
    }

    public void setYjydrq(String yjydrq)
    {
        this.yjydrq = yjydrq;
    }

    public String getQyrq()
    {
        return qyrq;
    }

    public void setQyrq(String qyrq)
    {
        this.qyrq = qyrq;
    }

    public String getYdrq()
    {
        return ydrq;
    }

    public void setYdrq(String ydrq)
    {
        this.ydrq = ydrq;
    }

    public List<String> getWjms() {
        return wjms;
    }

    public void setWjms(List<String> wjms) {
        this.wjms = wjms;
    }


    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getCxnr() {
        return cxnr;
    }

    public void setCxnr(String cxnr) {
        this.cxnr = cxnr;
    }

    public String getYbzt_flg() {
        return ybzt_flg;
    }
    public void setYbzt_flg(String ybzt_flg) {
        this.ybzt_flg = ybzt_flg;
    }
    public String getPrint_flg()
    {
        return print_flg;
    }
    public void setPrint_flg(String print_flg)
    {
        this.print_flg = print_flg;
    }
    public String getJcdm()
    {
        return jcdm;
    }
    public void setJcdm(String jcdm)
    {
        this.jcdm = jcdm;
    }
    public String getLxdm()
    {
        return lxdm;
    }
    public void setLxdm(String lxdm)
    {
        this.lxdm = lxdm;
    }
    public String getSerial()
    {
        return serial;
    }
    public void setSerial(String serial)
    {
        this.serial = serial;
    }
    public String getGzlxmc() {
        return gzlxmc;
    }
    public void setGzlxmc(String gzlxmc) {
        this.gzlxmc = gzlxmc;
    }
    public String getGzlx() {
        return gzlx;
    }
    public void setGzlx(String gzlx) {
        this.gzlx = gzlx;
    }
    public String getSfzqmc() {
        return sfzqmc;
    }
    public void setSfzqmc(String sfzqmc) {
        this.sfzqmc = sfzqmc;
    }
    public String getBgmbmc() {
        return bgmbmc;
    }
    public void setBgmbmc(String bgmbmc) {
        this.bgmbmc = bgmbmc;
    }
    public String getTjzq()
    {
        return tjzq;
    }
    public void setTjzq(String tjzq)
    {
        this.tjzq = tjzq;
    }
    public String getJcxmid()
    {
        return jcxmid;
    }
    public void setJcxmid(String jcxmid)
    {
        this.jcxmid = jcxmid;
    }
    public String getFlmc()
    {
        return flmc;
    }
    public void setFlmc(String flmc)
    {
        this.flmc = flmc;
    }
    public String getZflmc()
    {
        return zflmc;
    }
    public void setZflmc(String zflmc)
    {
        this.zflmc = zflmc;
    }
    public String getCn()
    {
        return cn;
    }
    public void setCn(String cn)
    {
        this.cn = cn;
    }
    public String[] getJyjgs()
    {
        return jyjgs;
    }
    public void setJyjgs(String[] jyjgs)
    {
        this.jyjgs = jyjgs;
        for (int i = 0; i < jyjgs.length; i++){
            this.jyjgs[i]=this.jyjgs[i].replace("'","");
        }
    }
    public String getBgmb()
    {
        return bgmb;
    }
    public void setBgmb(String bgmb)
    {
        this.bgmb = bgmb;
    }
    public String getFl()
    {
        return fl;
    }
    public void setFl(String fl)
    {
        this.fl = fl;
    }
    public String getZfl()
    {
        return zfl;
    }
    public void setZfl(String zfl)
    {
        this.zfl = zfl;
    }
    public Map<String, String> getParam()
    {
        return param;
    }
    public void setParam(Map<String, String> param)
    {
        this.param = param;
    }
    public String getSfzq()
    {
        return sfzq;
    }
    public void setSfzq(String sfzq)
    {
        this.sfzq = sfzq;
    }
    public String getLcfk()
    {
        return lcfk;
    }
    public void setLcfk(String lcfk)
    {
        this.lcfk = lcfk;
    }
    public String getSfbz()
    {
        return sfbz;
    }
    public void setSfbz(String sfbz)
    {
        this.sfbz = sfbz;
    }
    public String getPdflx() {
        return pdflx;
    }
    public void setPdflx(String pdflx) {
        this.pdflx = pdflx;
    }
    public String getWordlx() {
        return wordlx;
    }
    public void setWordlx(String wordlx) {
        this.wordlx = wordlx;
    }
    public String getJclx() {
        return jclx;
    }
    public void setJclx(String jclx) {
        this.jclx = jclx;
    }
    public String getBgbj() {
        return bgbj;
    }
    public void setBgbj(String bgbj) {
        this.bgbj = bgbj;
    }
    public String getYhid()
    {
        return yhid;
    }
    public void setYhid(String yhid)
    {
        this.yhid = yhid;
    }
    public String getLrsjStart()
    {
        return lrsjStart;
    }
    public void setLrsjStart(String lrsjStart)
    {
        this.lrsjStart = lrsjStart;
    }
    public String getLrsjEnd()
    {
        return lrsjEnd;
    }
    public void setLrsjEnd(String lrsjEnd)
    {
        this.lrsjEnd = lrsjEnd;
    }
    public String getJcxmkzcs()
    {
        return jcxmkzcs;
    }
    public void setJcxmkzcs(String jcxmkzcs)
    {
        this.jcxmkzcs = jcxmkzcs;
    }
    public String getLx() {
        return lx;
    }
    public void setLx(String lx) {
        this.lx = lx;
    }
    public String getYy() {
        return yy;
    }
    public void setYy(String yy) {
        this.yy = yy;
    }
    public String getCskz1_flg()
    {
        return cskz1_flg;
    }
    public void setCskz1_flg(String cskz1_flg)
    {
        this.cskz1_flg = cskz1_flg;
    }
    public String getCskz2_flg()
    {
        return cskz2_flg;
    }
    public void setCskz2_flg(String cskz2_flg)
    {
        this.cskz2_flg = cskz2_flg;
    }
    public String getCskz3_flg()
    {
        return cskz3_flg;
    }
    public void setCskz3_flg(String cskz3_flg)
    {
        this.cskz3_flg = cskz3_flg;
    }
    public String getCskz4_flg()
    {
        return cskz4_flg;
    }
    public void setCskz4_flg(String cskz4_flg)
    {
        this.cskz4_flg = cskz4_flg;
    }
    public String getSfflg() {
        return sfflg;
    }
    public void setSfflg(String sfflg) {
        this.sfflg = sfflg;
    }
    public List<String> getHbfls() {
        return hbfls;
    }
    public void setHbfls(String hbfls) {
        String[] str = hbfls.split(",");
        this.hbfls = Arrays.asList(str);
    }
    public void setHbfls(List<String> hbfls) {
        if(hbfls!=null && hbfls.size() > 0){
            hbfls.replaceAll(s -> s.replace("[", "").replace("]", "").trim());
        }
        this.hbfls = hbfls;
    }
    public List<String> getDbs() {
        return dbs;
    }
    public void setDbs(List<String> dbs) {
        this.dbs = dbs;
    }
    public String getHbfl() {
        return hbfl;
    }
    public void setHbfl(String hbfl) {
        this.hbfl = hbfl;
    }
    public String getHbzfl() {
        return hbzfl;
    }
    public void setHbzfl(String hbzfl) {
        this.hbzfl = hbzfl;
    }
    public String getZq() {
        return zq;
    }
    public void setZq(String zq) {
        this.zq = zq;
    }
    public String getSfjsmc() {
        return sfjsmc;
    }
    public void setSfjsmc(String sfjsmc) {
        this.sfjsmc = sfjsmc;
    }
    public String getSftjmc() {
        return sftjmc;
    }
    public void setSftjmc(String sftjmc) {
        this.sftjmc = sftjmc;
    }
    public String getSfsfmc() {
        return sfsfmc;
    }
    public void setSfsfmc(String sfsfmc) {
        this.sfsfmc = sfsfmc;
    }
    public List<String> getYbbhs() {
        return ybbhs;
    }
    public void setYbbhs(List<String> ybbhs) {
        this.ybbhs = ybbhs;
    }
    public String getFjsrq() {
        return fjsrq;
    }
    public void setFjsrq(String fjsrq) {
        this.fjsrq = fjsrq;
    }
    public String getPx()
    {
        return px;
    }
    public void setPx(String px)
    {
        this.px = px;
    }
    public String getJcxm()
    {
        return jcxm;
    }
    public void setJcxm(String jcxm)
    {
        this.jcxm = jcxm;
    }
    public String getCskz5()
    {
        return cskz5;
    }
    public void setCskz5(String cskz5)
    {
        this.cskz5 = cskz5;
    }
    public String getCskz2()
    {
        return cskz2;
    }
    public void setCskz2(String cskz2)
    {
        this.cskz2 = cskz2;
    }
    public String getCskz3()
    {
        return cskz3;
    }
    public void setCskz3(String cskz3)
    {
        this.cskz3 = cskz3;
    }
    public String getCskz4()
    {
        return cskz4;
    }
    public void setCskz4(String cskz4)
    {
        this.cskz4 = cskz4;
    }
    public String getSign()
    {
        return sign;
    }
    public void setSign(String sign)
    {
        this.sign = sign;
    }
    public List<String> getSjhbs()
    {
        return sjhbs;
    }
    public void setSjhbs(List<String> sjhbs)
    {
        this.sjhbs = sjhbs;
    }
    public String getJstj()
    {
        return jstj;
    }
    public void setJstj(String jstj)
    {
        this.jstj = jstj;
    }
    public String getYbztmc()
    {
        return ybztmc;
    }
    public void setYbztmc(String ybztmc)
    {
        this.ybztmc = ybztmc;
    }
    public List<SjybztDto> getSjybzts()
    {
        return sjybzts;
    }
    public void setSjybzts(List<SjybztDto> sjybzts)
    {
        this.sjybzts = sjybzts;
    }
    public List<String> getZts()
    {
        return zts;
    }
    public void setZts(List<String> zts)
    {
        this.zts = zts;
    }
    public String getSfjs()
    {
        return sfjs;
    }
    public void setSfjs(String sfjs)
    {
        this.sfjs = sfjs;
    }
    public String getSftj()
    {
        return sftj;
    }
    public void setSftj(String sftj)
    {
        this.sftj = sftj;
    }
    public String getSfsy()
    {
        return sfsy;
    }
    public void setSfsy(String sfsy)
    {
        this.sfsy = sfsy;
    }
    //检索周期
    private Map<String,String> zqs = new HashMap<>();
    public String getTj()
    {
        return tj;
    }
    public void setTj(String tj)
    {
        this.tj = tj;
    }
    public int getXh() {
        return xh;
    }
    public void setXh(int xh) {
        this.xh = xh;
    }
    //参数扩展1
    private String cskz1;

    public String getCskz1()
    {
        return cskz1;
    }
    public void setCskz1(String cskz1)
    {
        this.cskz1 = cskz1;
    }

    public String getMbdm() {
        return mbdm;
    }
    public void setMbdm(String mbdm) {
        this.mbdm = mbdm;
    }
    public String getStatus()
    {
        return status;
    }
    public void setStatus(String status)
    {
        this.status = status;
    }
    public String getXg_flg()
    {
        return xg_flg;
    }
    public void setXg_flg(String xg_flg)
    {
        this.xg_flg = xg_flg;
    }
    public String getFkrq(){
        return fkrq;
    }
    public void setFkrq(String fkrq)
    {
        this.fkrq = fkrq;
    }
    public void setYblxs(String[] yblxs){
        this.yblxs = yblxs;
        for (int i = 0; i < yblxs.length; i++){
            this.yblxs[i] = this.yblxs[i].replace("'", "");
        }
    }

    public void setDwids(String[] dwids) {
        this.dwids=dwids;
        for (int i = 0; i < dwids.length; i++){
            this.dwids[i]=this.dwids[i].replace("'","");
        }
    }
    public String getJcxmmc(){
        return jcxmmc;
    }
    public void setJcxmmc(String jcxmmc){
        this.jcxmmc = jcxmmc;
    }
    public String getXbmc(){
        return xbmc;
    }
    public void setXbmc(String xbmc){
        this.xbmc = xbmc;
    }
    public String getCsmc(){
        return csmc;
    }
    public void setCsmc(String csmc){
        this.csmc = csmc;
    }
    public String getYsid() {
        return ysid;
    }
    public void setYsid(String ysid) {
        this.ysid = ysid;
    }

    public String getWxid() {
        return wxid;
    }
    public void setWxid(String wxid) {
        this.wxid = wxid;
    }

    public List<String> getLczzs() {
        return lczzs;
    }

    public void setLczzs(List<String> lczzs) {
        this.lczzs = lczzs;
    }

    public List<String> getJcxmids() {
        return jcxmids;
    }

    public void setJcxmids(List<String> jcxmids) {
        this.jcxmids = jcxmids;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    public String getDbm() {
        return dbm;
    }

    public void setDbm(String dbm) {
        this.dbm = dbm;
    }

    public List<SjlczzDto> getSjlczzs() {
        return sjlczzs;
    }

    public void setSjlczzs(List<SjlczzDto> sjlczzs) {
        this.sjlczzs = sjlczzs;
    }

    public List<SjjcxmDto> getSjjcxms() {
        return sjjcxms;
    }

    public void setSjjcxms(List<SjjcxmDto> sjjcxms) {
        this.sjjcxms = sjjcxms;
    }

    public String getStartpage() {
        return startpage;
    }

    public void setStartpage(String startpage) {
        this.startpage = startpage;
    }

    public String getApplydatestart() {
        return applydatestart;
    }

    public void setApplydatestart(String applydatestart) {
        this.applydatestart = applydatestart;
    }

    public String getSamplecode() {
        return samplecode;
    }

    public void setSamplecode(String samplecode) {
        this.samplecode = samplecode;
    }
    public String getDwmc(){
        return dwmc;
    }

    public void setDwmc(String dwmc){
        this.dwmc = dwmc;
    }
    public List<SjqqjcDto> getSjqqjcs() {
        return sjqqjcs;
    }

    public void setSjqqjcs(List<SjqqjcDto> sjqqjcs) {
        this.sjqqjcs = sjqqjcs;
    }

    public List<SjgzbyDto> getSjgzbys() {
        return sjgzbys;
    }

    public void setSjgzbys(List<SjgzbyDto> sjgzbys) {
        this.sjgzbys = sjgzbys;
    }

    public String getJsrqstart(){
        return jsrqstart;
    }

    public void setJsrqstart(String jsrqstart){
        this.jsrqstart = jsrqstart;
    }

    public String getJsrqend(){
        return jsrqend;
    }

    public void setJsrqend(String jsrqend){
        this.jsrqend = jsrqend;
    }

    public String getFjid() {
        return fjid;
    }

    public void setFjid(String fjid) {
        this.fjid = fjid;
    }

    public List<String> getFjids() {
        return fjids;
    }

    public void setFjids(List<String> fjids) {
        this.fjids = fjids;
    }

    public String getYwlx() {
        return ywlx;
    }

    public void setYwlx(String ywlx) {
        this.ywlx = ywlx;
    }
    public String getYjxmmc() {
        return yjxmmc;
    }
    public void setYjxmmc(String yjxmmc) {
        this.yjxmmc = yjxmmc;
    }
    public String getKsmc()
    {
        return ksmc;
    }
    public void setKsmc(String ksmc)
    {
        this.ksmc = ksmc;
    }
    public String getGzbymc()
    {
        return gzbymc;
    }
    public void setGzbymc(String gzbymc)
    {
        this.gzbymc = gzbymc;
    }
    public String getQqjcmc()
    {
        return qqjcmc;
    }
    public void setQqjcmc(String qqjcmc)
    {
        this.qqjcmc = qqjcmc;
    }
    public String getLczzmc()
    {
        return lczzmc;
    }
    public void setLczzmc(String lczzmc)
    {
        this.lczzmc = lczzmc;
    }
    public List<String> getBys()
    {
        return bys;
    }
    public void setBys(List<String> bys)
    {
        this.bys = bys;
    }
    public String[] getDwids()
    {
        return dwids;
    }
    public String[] getYblxs()
    {
        return yblxs;
    }
    public String getSqlParam() {
        return SqlParam;
    }
    public void setSqlParam(String sqlParam) {
        SqlParam = sqlParam;
    }
    public String getYblx_flg() {
        return yblx_flg;
    }
    public void setYblx_flg(String yblx_flg) {
        this.yblx_flg = yblx_flg;
    }
    public String getSjdwxx_flg() {
        return sjdwxx_flg;
    }
    public void setSjdwxx_flg(String sjdwxx_flg) {
        this.sjdwxx_flg = sjdwxx_flg;
    }
    public String getFkmc()
    {
        return fkmc;
    }
    public void setFkmc(String fkmc)
    {
        this.fkmc = fkmc;
    }

    public String getZsxm()
    {
        return zsxm;
    }

    public void setZsxm(String zsxm)
    {
        this.zsxm = zsxm;
    }
    public String getDdid() {
        return ddid;
    }
    public void setDdid(String ddid) {
        this.ddid = ddid;
    }
    public String getWjlj() {
        return wjlj;
    }
    public void setWjlj(String wjlj) {
        this.wjlj = wjlj;
    }

    public String getWechatid() {
        return wechatid;
    }

    public void setWechatid(String wechatid) {
        this.wechatid = wechatid;
    }

    public String getHblx() {
        return hblx;
    }

    public void setHblx(String hblx) {
        this.hblx = hblx;
    }
    public String getWordfjid() {
        return wordfjid;
    }
    public void setWordfjid(String wordfjid) {
        this.wordfjid = wordfjid;
    }
    public String getW_ywlx() {
        return w_ywlx;
    }
    public void setW_ywlx(String w_ywlx) {
        this.w_ywlx = w_ywlx;
    }
    public String getFsfs() {
        return fsfs;
    }
    public void setFsfs(String fsfs) {
        this.fsfs = fsfs;
    }
    public String getYx() {
        return yx;
    }
    public void setYx(String yx) {
        this.yx = yx;
    }
    public String getWordwjm() {
        return wordwjm;
    }
    public void setWordwjm(String wordwjm) {
        this.wordwjm = wordwjm;
    }
    public String getWjm() {
        return wjm;
    }
    public void setWjm(String wjm) {
        this.wjm = wjm;
    }
    public String getJsrqMstart() {
        return jsrqMstart;
    }
    public void setJsrqMstart(String jsrqMstart) {
        this.jsrqMstart = jsrqMstart;
    }
    public String getJsrqMend() {
        return jsrqMend;
    }
    public void setJsrqMend(String jsrqMend) {
        this.jsrqMend = jsrqMend;
    }
    public String getJsrqYstart() {
        return jsrqYstart;
    }
    public void setJsrqYstart(String jsrqYstart) {
        this.jsrqYstart = jsrqYstart;
    }
    public String getJsrqYend() {
        return jsrqYend;
    }
    public void setJsrqYend(String jsrqYend) {
        this.jsrqYend = jsrqYend;
    }
    public List<String> getRqs() {
        return rqs;
    }
    public void setRqs(List<String> rqs) {
        this.rqs = rqs;
    }
    public String getBgrqstart()
    {
        return bgrqstart;
    }
    public void setBgrqstart(String bgrqstart)
    {
        this.bgrqstart = bgrqstart;
    }
    public String getBgrqend()
    {
        return bgrqend;
    }
    public void setBgrqend(String bgrqend)
    {
        this.bgrqend = bgrqend;
    }
    public String getBgrqMstart()
    {
        return bgrqMstart;
    }
    public void setBgrqMstart(String bgrqMstart)
    {
        this.bgrqMstart = bgrqMstart;
    }
    public String getBgrqMend()
    {
        return bgrqMend;
    }
    public void setBgrqMend(String bgrqMend)
    {
        this.bgrqMend = bgrqMend;
    }
    public String getBgrqYstart()
    {
        return bgrqYstart;
    }
    public void setBgrqYstart(String bgrqYstart)
    {
        this.bgrqYstart = bgrqYstart;
    }
    public String getBgrqYend()
    {
        return bgrqYend;
    }
    public void setBgrqYend(String bgrqYend)
    {
        this.bgrqYend = bgrqYend;
    }
    public Map<String,String> getZqs() {
        return zqs;
    }
    public void setZqs(Map<String,String> zqs) {
        this.zqs = zqs;
    }
    public String getHbcskz2() {
        return hbcskz2;
    }
    public void setHbcskz2(String hbcskz2) {
        this.hbcskz2 = hbcskz2;
    }
    public String[] getSjkzcs1()
    {
        return sjkzcs1;
    }
    public void setSjkzcs1(String[] sjkzcs1)
    {
        this.sjkzcs1 = sjkzcs1;
        for (int i = 0; i < sjkzcs1.length; i++){
            this.sjkzcs1[i]=this.sjkzcs1[i].replace("'","");
        }
    }
    public String[] getSjkzcs2()
    {
        return sjkzcs2;
    }
    public void setSjkzcs2(String[] sjkzcs2)
    {
        this.sjkzcs2 = sjkzcs2;
        for (int i = 0; i < sjkzcs2.length; i++){
            this.sjkzcs2[i]=this.sjkzcs2[i].replace("'","");
        }
    }
    public String[] getSjkzcs3()
    {
        return sjkzcs3;

    }
    public void setSjkzcs3(String[] sjkzcs3)
    {
        this.sjkzcs3 = sjkzcs3;
        for (int i = 0; i < sjkzcs3.length; i++){
            this.sjkzcs3[i]=this.sjkzcs3[i].replace("'","");
        }
    }
    public String[] getSjkzcs4()
    {
        return sjkzcs4;
    }
    public void setSjkzcs4(String[] sjkzcs4)
    {
        this.sjkzcs4 = sjkzcs4;
        for (int i = 0; i < sjkzcs4.length; i++){
            this.sjkzcs4[i]=this.sjkzcs4[i].replace("'","");
        }
    }
    public String getJcxmcskz() {
        return jcxmcskz;
    }
    public void setJcxmcskz(String jcxmcskz) {
        this.jcxmcskz = jcxmcskz;
    }
    public String[] getTjzds() {
        return tjzds;
    }
    public void setTjzds(String[] tjzds) {
        this.tjzds = tjzds;
        for (int i = 0; i < tjzds.length; i++){
            this.tjzds[i]=this.tjzds[i].replace("\"","").replace("[","").replace("]","").replace("，",",");
        }
    }
    public String[] getSelectzds() {
        return selectzds;
    }
    public void setSelectzds(String[] selectzds) {
        this.selectzds = selectzds;
        for (int i = 0; i < selectzds.length; i++){
            this.selectzds[i]=this.selectzds[i].replace("\"","").replace("[","").replace("]","").replace("，",",");
        }
    }
    public String[] getGroupzds() {
        return groupzds;
    }
    public void setGroupzds(String[] groupzds) {
        this.groupzds = groupzds;
        for (int i = 0; i < groupzds.length; i++){
            this.groupzds[i]=this.groupzds[i].replace("\"","").replace("[","").replace("]","").replace("，",",");
        }
    }
    public String[] getMainSelects() {
        return mainSelects;
    }
    public void setMainSelects(String[] mainSelects) {
        this.mainSelects = mainSelects;
        for (int i = 0; i < mainSelects.length; i++){
            this.mainSelects[i]=this.mainSelects[i].replace("\"","").replace("[","").replace("]","").replace("，",",");
        }
    }
    public String[] getMainGroups() {
        return mainGroups;
    }
    public void setMainGroups(String[] mainGroups) {
        this.mainGroups = mainGroups;
        for (int i = 0; i < mainGroups.length; i++){
            this.mainGroups[i]=this.mainGroups[i].replace("\"","").replace("[","").replace("]","").replace("，",",");
        }
    }

    public String getJsrymc() {
        return jsrymc;
    }

    public void setJsrymc(String jsrymc) {
        this.jsrymc = jsrymc;
    }

    public String getSfzmmc() {
        return sfzmmc;
    }

    public void setSfzmmc(String sfzmmc) {
        this.sfzmmc = sfzmmc;
    }

    public String getZmxmmc() {
        return zmxmmc;
    }

    public void setZmxmmc(String zmxmmc) {
        this.zmxmmc = zmxmmc;
    }

    public String getZmmddmc() {
        return zmmddmc;
    }

    public void setZmmddmc(String zmmddmc) {
        this.zmmddmc = zmmddmc;
    }

    public String getBt() {
        return bt;
    }

    public void setBt(String bt) {
        this.bt = bt;
    }

    public String getNr() {
        return nr;
    }

    public void setNr(String nr) {
        this.nr = nr;
    }

    public String getOld_fkje() {
        return old_fkje;
    }

    public void setOld_fkje(String old_fkje) {
        this.old_fkje = old_fkje;
    }

    public String getSyrqstart() {
        return syrqstart;
    }

    public void setSyrqstart(String syrqstart) {
        this.syrqstart = syrqstart;
    }

    public String getSyrqend() {
        return syrqend;
    }

    public void setSyrqend(String syrqend) {
        this.syrqend = syrqend;
    }

    public String getSyrqflg() {
        return syrqflg;
    }

    public void setSyrqflg(String syrqflg) {
        this.syrqflg = syrqflg;
    }

    public String[] getJcxms() {
        return jcxms;
    }

    public void setJcxms(String[] jcxms) {
        this.jcxms = jcxms;
        for (int i = 0; i < jcxms.length; i++){
            this.jcxms[i]=this.jcxms[i].replace("'","");
        }
    }

    public String getJc_cskz2() {
        return jc_cskz2;
    }

    public void setJc_cskz2(String jc_cskz2) {
        this.jc_cskz2 = jc_cskz2;
    }

    public String getHospitalname() {
        return hospitalname;
    }

    public void setHospitalname(String hospitalname) {
        this.hospitalname = hospitalname;
    }

    public String getHospitalName_flg() {
        return hospitalName_flg;
    }

    public void setHospitalName_flg(String hospitalName_flg) {
        this.hospitalName_flg = hospitalName_flg;
    }

    public String getYyxxCskz1() {
        return yyxxCskz1;
    }

    public void setYyxxCskz1(String yyxxCskz1) {
        this.yyxxCskz1 = yyxxCskz1;
    }

    public String getSjqfmc() {
        return sjqfmc;
    }

    public void setSjqfmc(String sjqfmc) {
        this.sjqfmc = sjqfmc;
    }

    public String getKpsqmc() {
        return kpsqmc;
    }

    public void setKpsqmc(String kpsqmc) {
        this.kpsqmc = kpsqmc;
    }

    public String getDwjc() {
        return dwjc;
    }

    public void setDwjc(String dwjc) {
        this.dwjc = dwjc;
    }

    public List<String> getFjlxs() {
        return fjlxs;
    }

    public void setFjlxs(List<String> fjlxs) {
        this.fjlxs = fjlxs;
    }

    public String getYzid() {
        return yzid;
    }

    public void setYzid(String yzid) {
        this.yzid = yzid;
    }

    public String getAll_param() {
        return all_param;
    }

    public void setAll_param(String all_param) {
        this.all_param = all_param;
    }

    public String getSingle_flag() {
        return single_flag;
    }

    public void setSingle_flag(String single_flag) {
        this.single_flag = single_flag;
    }

    public List<String> getUserids() {
        return userids;
    }

    public void setUserids(List<String> userids) {
        this.userids = userids;
    }

    public List<String> getDbmcs() {
        return dbmcs;
    }

    public void setDbmcs(List<String> dbmcs) {
        this.dbmcs = dbmcs;
    }

    public List<String> getDbgws() {
        return dbgws;
    }

    public void setDbgws(List<String> dbgws) {
        this.dbgws = dbgws;
    }

    public String getWjmgz() {
        return wjmgz;
    }

    public void setWjmgz(String wjmgz) {
        this.wjmgz = wjmgz;
    }

    public String getYyxxCskz3() {
        return yyxxCskz3;
    }

    public void setYyxxCskz3(String yyxxCskz3) {
        this.yyxxCskz3 = yyxxCskz3;
    }

    public String getKylx() {
        return kylx;
    }

    public void setKylx(String kylx) {
        this.kylx = kylx;
    }

    public String[] getJcjgmc() {
        return jcjgmc;
    }

    public void setJcjgmc(String[] jcjgmc) {
        String str = StringUtils.join(Arrays.asList(jcjgmc), ",");
        jcjgmc = str.split(",|，| ");
        this.jcjgmc = jcjgmc;
        List<String> reslist=new ArrayList<>();
        for (int i = 0; i < jcjgmc.length; i++){
            if (StringUtil.isNotBlank(jcjgmc[i])){
                reslist.add(jcjgmc[i]);
            }
        }
        this.jcjgmc = reslist.toArray(new String[reslist.size()]);
    }

    public String getEntire() {
        return entire;
    }

    public void setEntire(String entire) {
        this.entire = entire;
    }

    public String getSjsj() {
        return sjsj;
    }

    public void setSjsj(String sjsj) {
        this.sjsj = sjsj;
    }

    public String[] getSfsfs() {
        return sfsfs;
    }

    public void setSfsfs(String[] sfsfs) {
        this.sfsfs = sfsfs;
        for (int i = 0; i < sfsfs.length; i++){
            this.sfsfs[i]=this.sfsfs[i].replace("'","");
        }
    }

    public String getCskz6() {
        return cskz6;
    }

    public void setCskz6(String cskz6) {
        this.cskz6 = cskz6;
    }

    public String getKyxm() {
        return kyxm;
    }

    public void setKyxm(String kyxm) {
        this.kyxm = kyxm;
    }

    public String getSfvip() {
        return sfvip;
    }

    public void setSfvip(String sfvip) {
        this.sfvip = sfvip;
    }

    public String getMxid() {
        return mxid;
    }

    public void setMxid(String mxid) {
        this.mxid = mxid;
    }


    public String getJcjg() { return jcjg; }

    public void setJcjg(String jcjg) { this.jcjg = jcjg; }

    public String getYsjg() { return ysjg; }

    public void setYsjg(String ysjg) { this.ysjg = ysjg; }

    public String getSfbj() {
        return sfbj;
    }
    public void setSfbj(String sfbj) {
        this.sfbj = sfbj;
    }
    public String getTkfs() {
        return tkfs;
    }
    public void setTkfs(String tkfs) {
        this.tkfs = tkfs;
    }
    public String getTkje() {
        return tkje;
    }
    public void setTkje(String tkje) {
        this.tkje = tkje;
    }
    public String getJczxm() {
        return jczxm;
    }
    public void setJczxm(String jczxm) {
        this.jczxm = jczxm;
    }
    public String getSjdwmc() {
        return sjdwmc;
    }
    public void setSjdwmc(String sjdwmc) {
        this.sjdwmc = sjdwmc;
    }
    public String getQtsyrq() {
        return qtsyrq;
    }
    public void setQtsyrq(String qtsyrq) {
        this.qtsyrq = qtsyrq;
    }
    public String getQtjcbj() {
        return qtjcbj;
    }
    public void setQtjcbj(String qtjcbj) {
        this.qtjcbj = qtjcbj;
    }
    public String getSfzmjc() {
        return sfzmjc;
    }
    public void setSfzmjc(String sfzmjc) {
        this.sfzmjc = sfzmjc;
    }
    public String getZmxm() {
        return zmxm;
    }
    public void setZmxm(String zmxm) {
        this.zmxm = zmxm;
    }
    public String getZmmdd() {
        return zmmdd;
    }
    public void setZmmdd(String zmmdd) {
        this.zmmdd = zmmdd;
    }
    public String getWbbm() {
        return wbbm;
    }
    public void setWbbm(String wbbm) {
        this.wbbm = wbbm;
    }
    public String getCwh() {
        return cwh;
    }
    public void setCwh(String cwh) {
        this.cwh = cwh;
    }
    public String getSfsc() {
        return sfsc;
    }
    public void setSfsc(String sfsc) {
        this.sfsc = sfsc;
    }
    public String getJcbjmc() {
        return jcbjmc;
    }
    public void setJcbjmc(String jcbjmc) {
        this.jcbjmc = jcbjmc;
    }
    public String getDjcbj() {
        return djcbj;
    }
    public void setDjcbj(String djcbj) {
        this.djcbj = djcbj;
    }
    public String getDsyrq() {
        return dsyrq;
    }
    public void setDsyrq(String dsyrq) {
        this.dsyrq = dsyrq;
    }
    public String getSfje() {
        return sfje;
    }
    public void setSfje(String sfje) {
        this.sfje = sfje;
    }
    public String getSyrq() {
        return syrq;
    }
    public void setSyrq(String syrq) {
        this.syrq = syrq;
    }
    public String getSfsf()
    {
        return sfsf;
    }
    public void setSfsf(String sfsf)
    {
        this.sfsf = sfsf;
    }
    public String getJcbj()
    {
        return jcbj;
    }
    public void setJcbj(String jcbj)
    {
        this.jcbj = jcbj;
    }
    public String getQtks(){
        return qtks;
    }
    public void setQtks(String qtks)
    {
        this.qtks = qtks;
    }
    public String getNldw(){
        return nldw;
    }
    public void setNldw(String nldw)
    {
        this.nldw = nldw;
    }
    //送检ID
    public String getSjid() {
        return sjid;
    }
    public void setSjid(String sjid){
        this.sjid = sjid;
    }
    //患者姓名
    public String getHzxm() {
        return hzxm;
    }
    public void setHzxm(String hzxm){
        this.hzxm = hzxm;
    }
    //性别
    public String getXb() {
        return xb;
    }
    public void setXb(String xb) {
        this.xb = xb;
    }
    //年龄
    public String getNl() {
        return nl;
    }
    public void setNl(String nl){
        this.nl = nl;
    }
    //电话
    public String getDh() {
        return dh;
    }
    public void setDh(String dh){
        this.dh = dh;
    }
    //送检单位
    public String getSjdw() {
        return sjdw;
    }
    public void setSjdw(String sjdw){
        this.sjdw = sjdw;
    }
    //科室
    public String getKs() {
        return ks;
    }
    public void setKs(String ks){
        this.ks = ks;
    }
    //送检医生  暂时不关联
    public String getSjys() {
        return sjys;
    }
    public void setSjys(String sjys){
        this.sjys = sjys;
    }
    //医生电话
    public String getYsdh() {
        return ysdh;
    }
    public void setYsdh(String ysdh){
        this.ysdh = ysdh;
    }
    //代表  关联微信用户
    public String getDb() {
        return db;
    }
    public void setDb(String db){
        this.db = db;
    }
    //标本类型  关联基础数据
    public String getYblx() {
        return yblx;
    }
    public void setYblx(String yblx){
        this.yblx = yblx;
    }
    //标本类型名称 标本类型为其他的时候填写
    public String getYblxmc() {
        return yblxmc;
    }
    public void setYblxmc(String yblxmc){
        this.yblxmc = yblxmc;
    }
    //标本体积 任意填写
    public String getYbtj() {
        return ybtj;
    }
    public void setYbtj(String ybtj){
        this.ybtj = ybtj;
    }
    //住院号
    public String getZyh() {
        return zyh;
    }
    public void setZyh(String zyh){
        this.zyh = zyh;
    }
    //标本编号
    public String getYbbh() {
        return ybbh;
    }
    public void setYbbh(String ybbh){
        this.ybbh = ybbh;
    }
    //采样日期
    public String getCyrq() {
        return cyrq;
    }
    public void setCyrq(String cyrq){
        this.cyrq = cyrq;
    }
    //接收日期
    public String getJsrq() {
        return jsrq;
    }
    public void setJsrq(String jsrq){
        this.jsrq = jsrq;
    }
    public String getJsry()
    {
        return jsry;
    }
    public void setJsry(String jsry)
    {
        this.jsry = jsry;
    }
    //报告日期
    public String getBgrq() {
        return bgrq;
    }
    public void setBgrq(String bgrq){
        this.bgrq = bgrq;
    }
    //临床症状
    public String getLczz() {
        return lczz;
    }
    public void setLczz(String lczz){
        this.lczz = lczz;
    }
    //前期诊断
    public String getQqzd() {
        return qqzd;
    }
    public void setQqzd(String qqzd){
        this.qqzd = qqzd;
    }
    //近期用药
    public String getJqyy() {
        return jqyy;
    }
    public void setJqyy(String jqyy){
        this.jqyy = jqyy;
    }
    //前期检测
    public String getQqjc() {
        return qqjc;
    }
    public void setQqjc(String qqjc){
        this.qqjc = qqjc;
    }
    //送检报告
    public String getSjbg() {
        return sjbg;
    }
    public void setSjbg(String sjbg){
        this.sjbg = sjbg;
    }
    //检验结果  0：未检验 1：已检验  2：检验通过 3：检验未通过
    public String getJyjg() {
        return jyjg;
    }
    public void setJyjg(String jyjg){
        this.jyjg = jyjg;
    }
    //快递类型
    public String getKdlx() {
        return kdlx;
    }
    public void setKdlx(String kdlx) {
        this.kdlx = kdlx;
    }
    //快递号
    public String getKdh() {
        return kdh;
    }
    public void setKdh(String kdh) {
        this.kdh = kdh;
    }
    //快递费用
    public String getKdfy() {
        return kdfy;
    }
    public void setKdfy(String kdfy) {
        this.kdfy = kdfy;
    }
    //备注
    public String getBz() {
        return bz;
    }
    public void setBz(String bz) {
        this.bz = bz;
    }

    public String getZt() {
        return zt;
    }
    public void setZt(String zt) {
        this.zt = zt;
    }

    public String getFkbj() {
        return fkbj;
    }
    public void setFkbj(String fkbj) {
        this.fkbj = fkbj;
    }

    public String getFkje() {
        return fkje;
    }
    public void setFkje(String fkje) {
        this.fkje = fkje;
    }

    public String getNbbm() {
        return nbbm;
    }
    public void setNbbm(String nbbm) {
        this.nbbm = nbbm;
    }

    public String getSjqf() {
        return sjqf;
    }
    public void setSjqf(String sjqf) {
        this.sjqf = sjqf;
    }
    public String getKpsq() {
        return kpsq;
    }
    public void setKpsq(String kpsq) {
        this.kpsq = kpsq;
    }

	public String getCyrqstart() {
		return cyrqstart;
	}

	public void setCyrqstart(String cyrqstart) {
		this.cyrqstart = cyrqstart;
	}

	public String getCyrqend() {
		return cyrqend;
	}

	public void setCyrqend(String cyrqend) {
		this.cyrqend = cyrqend;
	}
}
