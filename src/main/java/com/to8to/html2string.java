package com.to8to;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author luoianun
 * @create 2018-12-10 15:21
 * @desc html字符串处理
 **/
public class html2string extends UDF {

    public String evaluate(String tags) {
        tags = StringEscapeUtils.unescapeHtml4(tags);
        String output ="";
        if(tags.indexOf("toutiao")==-1)
        {
            output = html2string.Html2Text(tags);
        }else
        {
            //url为今日头条的,特殊处理，取articleInfo.title + articleInfo.content
            String str_title = "";
            String str_content = "";
            output = html2string.Html2Text(tags);
            //取articleInfo.title
            Pattern p_title = Pattern.compile("title: '[^']+'");
            Matcher m_title = p_title.matcher(output);
            if(m_title.find())
            {
                str_title = m_title.group();
                str_title = str_title.replaceAll("title: '|'","");
            }
            //取articleInfo.content
            Pattern p_content = Pattern.compile("content: '[^']+'");
            Matcher m_content = p_content.matcher(output);
            if(m_content.find())
            {
                str_content = m_content.group();
                str_content = str_content.replaceAll("content: '|'","");
            }

            output = str_title + str_content;

        }
        return output;
    }
    public static String escapeRegularSpecialWord(String keyword) {
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(keyword)) {
            String[] regularSpecialWordArr = {"\\", "$", "(", ")", "*", "+", ".", "[", "]", "?",
                    "^", "{", "}", "|"};
            for (String key : regularSpecialWordArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
        }
        return keyword;
    }

    public static String Html2Text(String inputString){
        if (StringUtils.isEmpty(inputString)){
            return "";
        }
        String htmlStr = inputString; //含html标签的字符串
        String textStr ="";
        java.util.regex.Pattern p_script;
        java.util.regex.Matcher m_script;
        java.util.regex.Pattern p_style;
        java.util.regex.Matcher m_style;
        java.util.regex.Pattern p_html;
        java.util.regex.Matcher m_html;

        try{
            String regEx_script = "<[\\s]*?script[^>]*?>[^(BASE_DATA)]*?<[\\s]*?\\/[\\s]*?script[\\s]*?>"; //定义script的正则表达式{或<script[^>]*?>[\\s\\S]*?<\\/script> }
            String regEx_style = "<[\\s]*?style[^>]*?>[\\s\\S]*?<[\\s]*?\\/[\\s]*?style[\\s]*?>"; //定义style的正则表达式{或<style[^>]*?>[\\s\\S]*?<\\/style> }
            String regEx_html = "<[^>]+>"; //定义HTML标签的正则表达式

            p_script = Pattern.compile(regEx_script,Pattern.CASE_INSENSITIVE);
            m_script = p_script.matcher(htmlStr);
            htmlStr = m_script.replaceAll(""); //过滤script标签
            //System.out.println("=========>"+htmlStr);
            p_style = Pattern.compile(regEx_style,Pattern.CASE_INSENSITIVE);
            m_style = p_style.matcher(htmlStr);
            htmlStr = m_style.replaceAll(""); //过滤style标签

            p_html = Pattern.compile(regEx_html,Pattern.CASE_INSENSITIVE);
            m_html = p_html.matcher(htmlStr);
            htmlStr = m_html.replaceAll(""); //过滤html标签
            htmlStr = htmlStr.replace("  ", "");
            htmlStr = htmlStr.replace("\n", "");
            htmlStr = htmlStr.replace("\t", "");
            htmlStr = htmlStr.replace("nbsp;","");
            textStr = htmlStr.trim();
            textStr = htmlStr;
        }catch(Exception e){

        }
//        for(int i=0;i<5;i++){
//            if (textStr.contains("MicrosoftInternetExplorer")){
//                textStr = textStr.substring(64,textStr.length());
//            }
//        }


        return textStr;//返回文本字符串
    }

    public static void main(String[] args) throws IOException{
        String a = "\n" +
                "<!DOCTYPE html><html><head><meta charset=utf-8><meta http-equiv=x-dns-prefetch-control content=on><meta name=renderer content=webkit><link rel=dns-prefetch href=//s3.pstatp.com/ ><link rel=dns-prefetch href=//s3a.pstatp.com/ ><link rel=dns-prefetch href=//s3b.pstatp.com><link rel=dns-prefetch href=//p1.pstatp.com/ ><link rel=dns-prefetch href=//p3.pstatp.com/ ><meta http-equiv=Content-Type content=\"text/html; charset=utf-8\"><meta http-equiv=X-UA-Compatible content=\"IE=edge,chrome=1\"><meta name=viewport content=\"width=device-width,initial-scale=1,maximum-scale=1,minimum-scale=1,user-scalable=no,minimal-ui\"><meta name=360-site-verification content=b96e1758dfc9156a410a4fb9520c5956><meta name=360_ssp_verify content=2ae4ad39552c45425bddb738efda3dbb><meta name=google-site-verification content=3PYTTW0s7IAfkReV8wAECfjIdKY-bQeSkVTyJNZpBKE><meta name=shenma-site-verification content=34c05607e2a9430ad4249ed48faaf7cb_1432711730><meta name=baidu_union_verify content=b88dd3920f970845bad8ad9f90d687f7><meta name=domain_verify content=pmrgi33nmfuw4ir2ej2g65lunfqw6ltdn5wselbcm52wszbchirdqyztge3tenrsgq3dknjume2tayrvmqytemlfmiydimddgu4gcnzcfqrhi2lnmvjwc5tfei5dcnbwhazdcobuhe2dqobrpu><link rel=\"shortcut icon\" href=//s3a.pstatp.com/toutiao/resource/ntoutiao_web/static/image/favicon_8e9c9c7.ico type=image/x-icon><!--[if lt IE 9]>\n" +
                "  <p>您的浏览器版本过低，请<a href=\"http://browsehappy.com/\">升级浏览器</a></p>\n" +
                "<![endif]--><link rel=alternate media=\"only screen and (max-width: 640px)\" href=//m.toutiao.com/a6496651641545032205/ ><title>阿里巴巴190亿携手联想控股乐视，融创成为第二大股东！</title><meta name=keywords content=乐视,贾跃亭,柳传志,法拉第未来,孙宏斌><meta name=description content=#马云、孙宏斌、柳传志终于牵手。对外支持乐视的乐视电视、手机、网络用户，及广大乐视股票的股票来说都是一个重磅好消息，中。><script src=\"//s3.pstatp.com/toutiao/monitor/sdk/slardar.js?ver=20171221_1\" crossorigin=anonymous></script><script>window.Slardar && window.Slardar.install({\n" +
                "      sampleRate: 0.5,\n" +
                "      bid: 'toutiao_pc',\n" +
                "      pid: 'article_detail_new',\n" +
                "      ignoreAjax: [/\\/action_log\\//],\n" +
                "      ignoreStatic: [/\\.tanx\\.com\\//, /\\.alicdn\\.com\\//, /\\.mediav\\.com/, /\\.cnzz\\.com/]\n" +
                "    });</script><link rel=stylesheet href=//s3.pstatp.com/toutiao/static/css/page/index_node/index.f88d73647554602caf51be88195b170f.css><script>!function(e){function t(a){if(c[a])return c[a].exports;var o=c[a]={exports:{},id:a,loaded:!1};return e[a].call(o.exports,o,o.exports,t),o.loaded=!0,o.exports}var a=window.webpackJsonp;window.webpackJsonp=function(r,n){for(var p,f,s=0,l=[];s<r.length;s++)f=r[s],o[f]&&l.push.apply(l,o[f]),o[f]=0;for(p in n)Object.prototype.hasOwnProperty.call(n,p)&&(e[p]=n[p]);for(a&&a(r,n);l.length;)l.shift().call(null,t);if(n[0])return c[0]=0,t(0)};var c={},o={0:0};t.e=function(e,a){if(0===o[e])return a.call(null,t);if(void 0!==o[e])o[e].push(a);else{o[e]=[a];var c=document.getElementsByTagName(\"head\")[0],r=document.createElement(\"script\");r.type=\"text/javascript\",r.charset=\"utf-8\",r.async=!0,r.src=t.p+\"static/js/\"+e+\".\"+{1:\"5fd9e0630cb7a6abc41a\",2:\"90fe76a3c3ba29f2a75c\",3:\"6cef927c4a188ed50d56\",4:\"3e7f634128257bf2abef\",5:\"0a47aba3bfcdf3f4f39f\"}[e]+\".js\",c.appendChild(r)}},t.m=e,t.c=c,t.p=\"/toutiao/\",t.p=\"//s3.pstatp.com/toutiao/\"}([]);</script></head><body><div id=app></div><script src=//s3.pstatp.com/inapp/lib/raven.js crossorigin=anonymous></script><script>;(function(window) {\n" +
                "    // sentry\n" +
                "    window.Raven && Raven.config('//key@m.toutiao.com/log/sentry/v2/96', {\n" +
                "      whitelistUrls: [/pstatp\\.com/],\n" +
                "      sampleRate: 0.5,\n" +
                "      shouldSendCallback: function(data) {\n" +
                "        var ua = navigator && navigator.userAgent;\n" +
                "        var isDeviceOK = !/Mobile|Linux/i.test(navigator.userAgent);\n" +
                "        if (data.message && data.message.indexOf('p.tanx.com') !== -1) {\n" +
                "          return false;\n" +
                "        }\n" +
                "        return isDeviceOK;\n" +
                "      },\n" +
                "      tags: {\n" +
                "        bid: 'toutiao_pc',\n" +
                "        pid: 'article_detail_new'\n" +
                "      },\n" +
                "      autoBreadcrumbs: {\n" +
                "        'xhr': false,\n" +
                "        'console': true,\n" +
                "        'dom': true,\n" +
                "        'location': true\n" +
                "      }\n" +
                "    }).install();\n" +
                "  })(window);</script><script>var PAGE_SWITCH = {\"adScriptQihu\":true,\"adScriptTB\":true,\"anti_spam\":false,\"migScriptUrl\":\"//s3a.pstatp.com/toutiao/picc_mig/dist/img.min.js\",\"nineteen\":\"\",\"picVersion\":\"20180412_01\",\"qihuAdShow\":true,\"taVersion\":\"20171221_1\",\"ttAdShow\":true};</script><script>var BASE_DATA = {\n" +
                "    userInfo: {\n" +
                "      id: 0,\n" +
                "      userName: '',\n" +
                "      avatarUrl: '',\n" +
                "      isPgc: false,\n" +
                "      isOwner: false\n" +
                "    },\n" +
                "    headerInfo: {\n" +
                "      id: 0,\n" +
                "      isPgc: false,\n" +
                "      userName: '',\n" +
                "      avatarUrl: '',\n" +
                "      isHomePage: false,\n" +
                "      chineseTag: '传媒',\n" +
                "      crumbTag: 'search/?keyword=%E4%BC%A0%E5%AA%92',\n" +
                "      hasBar: true\n" +
                "    },\n" +
                "    articleInfo: {\n" +
                "      title: '阿里巴巴190亿携手联想控股乐视，融创成为第二大股东！',\n" +
                "      content: '&lt;div&gt;&lt;p&gt;&lt;span&gt;&lt;img src&#x3D;&quot;http://p99.pstatp.com/large/4aeb000570f30c8455ff&quot; img_width&#x3D;&quot;285&quot; img_height&#x3D;&quot;553&quot; alt&#x3D;&quot;阿里巴巴190亿携手联想控股乐视，融创成为第二大股东！&quot; inline&#x3D;&quot;0&quot;&gt;&lt;/span&gt;&lt;/p&gt;&lt;h1&gt;&lt;span&gt;马云、孙宏斌、柳传志终于牵手&lt;/span&gt;&lt;/h1&gt;&lt;p&gt;&lt;span&gt;重磅，一个让乐视、贾跃亭、孙宏斌都开心的消息，也应该是他们努力的结果。对外支持乐视的乐视电视、手机、网络用户，及广大乐视股票的股票来说都是一个重磅好消息，中传媒平台（cmcnp.com），智能传媒平台内容机器人+专家正在全网搜寻和智能分析，确认该消息的真实性，聂老师本人也和阿里内部人员确认了消息的正确性。脉脉平台官方发布后也做了实名澄清，应该这个消息是属实的。&lt;/span&gt;&lt;/p&gt;&lt;p&gt;&lt;span&gt;阿里曾经也是当年领投乐视的重要背后资本，这次重要关口不可能见死不救，乐视经过孙宏斌的调整逐步走入正轨，特别是融创的产业进行融合后，乐视的未来前途不可能限量。&lt;/span&gt;&lt;/p&gt;&lt;p&gt;&lt;span&gt;&lt;/span&gt;&lt;/p&gt;&lt;h1&gt;法拉第未来签署超10亿美元融资协议，贾跃亭绝地求生！&lt;/h1&gt;&lt;span&gt;&lt;p&gt;法拉第未来（以下简称FF）也将获得一笔超过10亿美元的投资。交易完成后，贾跃亭将推动FF生产基地尽快开工，以兑现他2019年上市销售的诺言。风雨飘摇中的FF造车终于落下一子，贾跃亭雄起了！&lt;/p&gt;&lt;p&gt;&lt;span&gt;&lt;img src&#x3D;&quot;http://p99.pstatp.com/large/4aeb0004d0469fd714e3&quot; img_width&#x3D;&quot;600&quot; img_height&#x3D;&quot;399&quot; alt&#x3D;&quot;阿里巴巴190亿携手联想控股乐视，融创成为第二大股东！&quot; inline&#x3D;&quot;0&quot;&gt;&lt;/span&gt;&lt;/p&gt;&lt;/span&gt;&lt;p&gt;&lt;span&gt;寂寞的贾跃亭，你真么看这个消息，最新关进展注本头条&lt;/span&gt;&lt;/p&gt;&lt;p&gt;&lt;span&gt;编辑：nq1919&lt;/span&gt;&lt;/p&gt;&lt;p&gt;&lt;span&gt;本文已经开通原创，欢迎打赏！&lt;/span&gt;&lt;/p&gt;&lt;/div&gt;',\n" +
                "      groupId: '6496651641545032205',\n" +
                "      itemId: '6496651641545032205',\n" +
                "      type: 2,\n" +
                "      subInfo: {\n" +
                "        isOriginal: true,\n" +
                "        source: '聂潜老师',\n" +
                "        time: '2017-12-07 12:06:23'\n" +
                "      },\n" +
                "      tagInfo: {\n" +
                "        tags: [{\"name\":\"乐视\"},{\"name\":\"贾跃亭\"},{\"name\":\"孙宏斌\"},{\"name\":\"联想\"},{\"name\":\"马云\"}],\n" +
                "        groupId: '6496651641545032205',\n" +
                "        itemId: '6496651641545032205',\n" +
                "        repin: 0,\n" +
                "      },\n" +
                "      has_extern_link: 0,\n" +
                "      coverImg: 'http://p9.pstatp.com/list/300x196/4aeb0004d0469fd714e3.jpg'\n" +
                "    },\n" +
                "    commentInfo: {\n" +
                "      groupId: '6496651641545032205',\n" +
                "      itemId: '6496651641545032205',\n" +
                "      comments_count: 29,\n" +
                "      ban_comment: 0\n" +
                "    },\n" +
                "    mediaInfo: {\n" +
                "      uid: '67758557545',\n" +
                "      name: '聂潜老师',\n" +
                "      avatar: '//p8.pstatp.com/large/da78000551a40b076212',\n" +
                "      openUrl: '/c/user/67758557545/',\n" +
                "      follow: false\n" +
                "    },\n" +
                "    pgcInfo: [{\"item_id\":\"6632195999420908046\",\"url\":\"/item/6632195999420908046\",\"title\":\"自动化+AI=工人阶级消失？\"},{\"item_id\":\"6631822178012643848\",\"url\":\"/item/6631822178012643848\",\"title\":\"科大讯飞陷危局，企业该如何缓解人工智能焦虑？\"},{\"item_id\":\"6631449970136318467\",\"url\":\"/item/6631449970136318467\",\"title\":\"最新预言：2019年3大科技趋势，让每一个企业都可能成为独角兽！\"},{\"item_id\":\"6631094184327512579\",\"url\":\"/item/6631094184327512579\",\"title\":\"中本聪神秘现身，比特币江湖要变天了？\"}],\n" +
                "    feedInfo: {\n" +
                "      url: '/api/pc/feed/',\n" +
                "      category: '__all__',\n" +
                "      initList: [{\"comments_count\":187,\"media_avatar_url\":\"//p9.pstatp.com/large/6ee4000496aaf06c9744\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"特朗普将这件事称为“国耻” 找中国谈判 这次中方出手了！\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"02:16\",\"source_url\":\"https://www.toutiao.com/group/6631069278709744131/\",\"source\":\"环球时报国际\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1595320,\"image_url\":\"//p3.pstatp.com/list/190x124/pgc-image/3e98675464fd433ea5baf39693ebbcd8\",\"group_id\":\"6631069278709744131\",\"is_related\":true,\"media_url\":\"/c/user/96455231977/\"},{\"comments_count\":7702,\"media_avatar_url\":\"//p3.pstatp.com/large/4e640001428ea3505cd7\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"最美“卖鱼西施”走红，弯腰杀鱼引人注目！网友：又要骗我去吃鱼\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"01:40\",\"source_url\":\"https://www.toutiao.com/group/6632152016141419021/\",\"source\":\"游戏嘚吧嘚\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":2943988,\"image_url\":\"//p1.pstatp.com/list/190x124/pgc-image/4156badbf8004b4699a956487234f515\",\"group_id\":\"6632152016141419021\",\"is_related\":true,\"media_url\":\"/c/user/80736611556/\"},{\"comments_count\":2590,\"media_avatar_url\":\"//p1.pstatp.com/large/8b5f0004d095b19edc97\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"他们一上台张国立就蒙了！冯小刚成龙和李冰冰亲自下台去搀扶！\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"03:09\",\"source_url\":\"https://www.toutiao.com/group/6632650855403225608/\",\"source\":\"爆笑剧乐部\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1466761,\"image_url\":\"//p1.pstatp.com/list/190x124/pgc-image/1fee417f33fe4d8d999716c1b76a45a8\",\"group_id\":\"6632650855403225608\",\"is_related\":true,\"media_url\":\"/c/user/100536012551/\"},{\"comments_count\":374,\"media_avatar_url\":\"//p3.pstatp.com/large/4d0006191bd1c54c84\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"台北市长选举重启验票 票箱标示3000张 结果呢……\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"02:26\",\"source_url\":\"https://www.toutiao.com/group/6631792738046050819/\",\"source\":\"CCTV4\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1818797,\"image_url\":\"//p1.pstatp.com/list/190x124/12f31000dd796781a5239\",\"group_id\":\"6631792738046050819\",\"is_related\":true,\"media_url\":\"/c/user/5834354287/\"},{\"comments_count\":15637,\"media_avatar_url\":\"//p2.pstatp.com/large/93300002af5d910d05f8\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"法国街头越来越可怕，政府调动战争级的军队！内乱随时会出现\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"01:51\",\"source_url\":\"https://www.toutiao.com/group/6632286354879283726/\",\"source\":\"环球台\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1509231,\"image_url\":\"//p3.pstatp.com/list/190x124/pgc-image/29482bd1b6e34efaa0e7880b0d62de78\",\"group_id\":\"6632286354879283726\",\"is_related\":true,\"media_url\":\"/c/user/96455180558/\"},{\"comments_count\":594,\"media_avatar_url\":\"//p1.pstatp.com/large/dc0e0000fd3322a274c1\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"上百辆坦克驶向巴基斯坦，印方抗议遭无视，巴铁：感谢兄弟援手\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"03:19\",\"source_url\":\"https://www.toutiao.com/group/6630654770862359047/\",\"source\":\"兵鉴堂\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1444096,\"image_url\":\"//p99.pstatp.com/list/190x124/pgc-image/200190eab5dc49ca80f218f5d223a2ae\",\"group_id\":\"6630654770862359047\",\"is_related\":true,\"media_url\":\"/c/user/55153509430/\"},{\"comments_count\":153,\"media_avatar_url\":\"//p6.pstatp.com/large/53e900048ebc06888d8b\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"小伙偷偷来做按摩，听见旁边声音不对劲，拉开帘子一看竟是前女友\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"02:03\",\"source_url\":\"https://www.toutiao.com/group/6631013307341865485/\",\"source\":\"嘻哈影视圈\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1570883,\"image_url\":\"//p3.pstatp.com/list/190x124/pgc-image/d2800f5181d14bd1b9b9bf778651d71f\",\"group_id\":\"6631013307341865485\",\"is_related\":true,\"media_url\":\"/c/user/77192765973/\"},{\"comments_count\":327,\"media_avatar_url\":\"//p8.pstatp.com/large/906100005d605bc62e6e\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"穿着朴素的男嘉宾刚上台就被灭灯，短片展示背景后，连孟非都呆了\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"04:56\",\"source_url\":\"https://www.toutiao.com/group/6631719336350319107/\",\"source\":\"热播综艺精选\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":2121168,\"image_url\":\"//p9.pstatp.com/list/190x124/pgc-image/0c5fa460c45247bc98e36a80934b2160\",\"group_id\":\"6631719336350319107\",\"is_related\":true,\"media_url\":\"/c/user/100113777398/\"},{\"comments_count\":6585,\"media_avatar_url\":\"//p8.pstatp.com/large/986b0002a1d9e64e96fc\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"吴京终于给“吃鸡”拍广告了，场面火爆，不输战狼二！\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"03:06\",\"source_url\":\"https://www.toutiao.com/group/6631859536716104200/\",\"source\":\"星球八放映室\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":2114444,\"image_url\":\"//p3.pstatp.com/list/190x124/12f05000a3f20719bf733\",\"group_id\":\"6631859536716104200\",\"is_related\":true,\"media_url\":\"/c/user/101445312540/\"},{\"comments_count\":6152,\"media_avatar_url\":\"//p1.pstatp.com/large/66c000014f8eda01e7fd\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"游客为看鳄鱼吃野猪 用面包诱拐野猪到河里 最后成这样\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"02:28\",\"source_url\":\"https://www.toutiao.com/group/6632545428338393608/\",\"source\":\"触摸地球\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1682128,\"image_url\":\"//p3.pstatp.com/list/190x124/pgc-image/b4fa71f9d75040289846532c7c843148\",\"group_id\":\"6632545428338393608\",\"is_related\":true,\"media_url\":\"/c/user/6598722706/\"},{\"comments_count\":306,\"media_avatar_url\":\"//p99.pstatp.com/large/dc1000002eac7c2959b4\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"张召忠：国产航母的消息把我吓到了，我都没想到，真是不得了！\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"03:30\",\"source_url\":\"https://www.toutiao.com/group/6631402805028454920/\",\"source\":\"一寸土诉一年木\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1225808,\"image_url\":\"//p9.pstatp.com/list/190x124/12d950002c8e6561c6ee3\",\"group_id\":\"6631402805028454920\",\"is_related\":true,\"media_url\":\"/c/user/105956377463/\"},{\"comments_count\":356,\"media_avatar_url\":\"//p2.pstatp.com/large/6ee4000441709317007b\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"国民党准备动手了！列出台湾当局要下台人员名单，一撸到底\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"01:25\",\"source_url\":\"https://www.toutiao.com/group/6630786580942422541/\",\"source\":\"环球扫描\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1862407,\"image_url\":\"//p99.pstatp.com/list/190x124/pgc-image/d4d177463849414bad48742c7801a362\",\"group_id\":\"6630786580942422541\",\"is_related\":true,\"media_url\":\"/c/user/97012153591/\"},{\"comments_count\":4637,\"media_avatar_url\":\"//p8.pstatp.com/large/b7230000dd46a2d57bea\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"嫦娥四号预计登陆月球背面“嫦娥之父”却收到大量关于外星人警告\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"03:19\",\"source_url\":\"https://www.toutiao.com/group/6632196422756205064/\",\"source\":\"中宏网新闻\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1489589,\"image_url\":\"//p3.pstatp.com/list/190x124/pgc-image/95da5d0d2ea246309247baed5fae91d7\",\"group_id\":\"6632196422756205064\",\"is_related\":true,\"media_url\":\"/c/user/98731095200/\"},{\"comments_count\":2980,\"media_avatar_url\":\"//p7.pstatp.com/large/13540016ae888cff0e52\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"被公交耽误的歌手！乘客上车以为是音响，没想到是司机在唱歌\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"02:02\",\"source_url\":\"https://www.toutiao.com/group/6630934133109424647/\",\"source\":\"闪电新闻\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1246155,\"image_url\":\"//p9.pstatp.com/list/190x124/pgc-image/RBDaZmBF5XcsXE\",\"group_id\":\"6630934133109424647\",\"is_related\":true,\"media_url\":\"/c/user/51050126444/\"},{\"comments_count\":480,\"media_avatar_url\":\"//p6.pstatp.com/large/888f0004a4e41f49a70a\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"41岁大叔太优秀，拒绝爆灯女孩，牵手23岁漂亮女嘉宾，让人羡慕！\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"15:48\",\"source_url\":\"https://www.toutiao.com/group/6630780045847708164/\",\"source\":\"白夜追综\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":1669098,\"image_url\":\"//p99.pstatp.com/list/190x124/pgc-image/909dde866dc640d5a25cff489d7912b1\",\"group_id\":\"6630780045847708164\",\"is_related\":true,\"media_url\":\"/c/user/100050543621/\"},{\"comments_count\":7120,\"media_avatar_url\":\"//p10.pstatp.com/large/fe9b00000e4abe63f7ad\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"四川一小伙习武15年，终成一招“空中漫步”，不简单\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"01:20\",\"source_url\":\"https://www.toutiao.com/group/6631273610780606990/\",\"source\":\"世界焦点录\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":2236501,\"image_url\":\"//p99.pstatp.com/list/190x124/pgc-image/54e5b1f89d8b49579750315d96711bf9\",\"group_id\":\"6631273610780606990\",\"is_related\":true,\"media_url\":\"/c/user/6082964307/\"},{\"comments_count\":7892,\"media_avatar_url\":\"//p3.pstatp.com/large/6cb0015291dfcad1b30\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"小伙一天之内住五家情侣酒店寻找摄像头！每家酒店都很好玩\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"10:45\",\"source_url\":\"https://www.toutiao.com/group/6624437042539397636/\",\"source\":\"敬汉卿\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":2960717,\"image_url\":\"//p99.pstatp.com/list/190x124/pgc-image/7b3446539e9f4126ab080ec654c3dfa1\",\"group_id\":\"6624437042539397636\",\"is_related\":true,\"media_url\":\"/c/user/5725027217/\"},{\"comments_count\":15172,\"media_avatar_url\":\"//p1.pstatp.com/large/bc20000b91968707dab\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"8秒钟 8车被撞 一死三伤 导致这场无妄之灾的肇事者却偷偷溜走了\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"27:58\",\"source_url\":\"https://www.toutiao.com/group/6630961208918802958/\",\"source\":\"央视网新闻\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":3608751,\"image_url\":\"//p99.pstatp.com/list/190x124/12af3000d2928fd510e39\",\"group_id\":\"6630961208918802958\",\"is_related\":true,\"media_url\":\"/c/user/50025817786/\"},{\"comments_count\":10350,\"media_avatar_url\":\"//p3.pstatp.com/large/41200033204317c2828\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"乌克兰挑事成功，北约终于行动了，俄罗斯孤立无援！\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"03:07\",\"source_url\":\"https://www.toutiao.com/group/6631920736661406215/\",\"source\":\"第一军情\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":2777275,\"image_url\":\"//p99.pstatp.com/list/190x124/pgc-image/a77c2c83458142658588dcd10f6fb652\",\"group_id\":\"6631920736661406215\",\"is_related\":true,\"media_url\":\"/c/user/6264649967/\"},{\"comments_count\":439,\"media_avatar_url\":\"//p4.pstatp.com/large/90610001986ebcba78e7\",\"is_feed_ad\":false,\"is_diversion_page\":false,\"title\":\"赵本山酒后开车遇交警，聪明人办法多，来看他是怎么办的\",\"single_mode\":true,\"gallary_image_count\":0,\"middle_mode\":false,\"has_video\":true,\"video_duration_str\":\"13:20\",\"source_url\":\"https://www.toutiao.com/group/6632144890786480648/\",\"source\":\"娱乐开FUN\",\"more_mode\":null,\"article_genre\":\"video\",\"has_gallery\":false,\"video_play_count\":3800118,\"image_url\":\"//p9.pstatp.com/list/190x124/pgc-image/a8d7b1dee8984e1b8a4ea333d5bcc18d\",\"group_id\":\"6632144890786480648\",\"is_related\":true,\"media_url\":\"/c/user/100122219380/\"}]\n" +
                "    },\n" +
                "    shareInfo: {\n" +
                "      shareUrl: 'https://m.toutiao.com/item/6496651641545032205/',\n" +
                "      abstrac";

        Document b = Jsoup.connect("https://www.toutiao.com/i6496651641545032205/?tt_from=weixin&utm_campaign=client_share&from=timeline&app=stock&utm_source=weixin&isappinstalled=0&iid=18677888467&utm_medium=toutiao_android&amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&amp=&wxshare_count=5&pbid=6496756857008195086").get();
        html2string ht = new html2string();

        //System.out.println("result==============>"+b.toString());
        System.out.println(ht.evaluate(b.toString()));


    }
}
