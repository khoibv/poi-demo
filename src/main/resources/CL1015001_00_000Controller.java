package jp.co.nissho_ele.acc.cl.cl1015;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.tabview.TabView;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TabChangeEvent;

import jp.co.nissho_ele.acc.base_package.bean.BaseInfo;
import jp.co.nissho_ele.acc.base_package.bean.CL1015001_01_000InBean;
import jp.co.nissho_ele.acc.base_package.bean.CL1015001_01_000OutBean;
import jp.co.nissho_ele.acc.base_package.bean.CL1015001_02_000InBean;
import jp.co.nissho_ele.acc.base_package.bean.CL1015001_02_000OutBean;
import jp.co.nissho_ele.acc.base_package.bean.CL1028001_03_000InBean;
import jp.co.nissho_ele.acc.base_package.bean.CL1028001_03_000OutBean;
import jp.co.nissho_ele.acc.base_package.bean.CM9007001_01_000InBean;
import jp.co.nissho_ele.acc.base_package.bean.CM9007001_01_000OutBean;
import jp.co.nissho_ele.acc.base_package.bean.CM9007001_02_000InBean;
import jp.co.nissho_ele.acc.base_package.bean.CM9007001_02_000OutBean;
import jp.co.nissho_ele.acc.base_package.common.util.ACCStringUtil;
import jp.co.nissho_ele.acc.base_package.entity.dto.CL1015001_02_000Rselect1Dto;
import jp.co.nissho_ele.acc.cl.cl1001.CL1001001_00_000Controller;
import jp.co.nissho_ele.acc.cl.cl1001.CL1001001_00_000Controller.TabData;
import jp.co.nissho_ele.acc.cl.cl1001.CL1001012_00_000Model;
import jp.co.nissho_ele.acc.framework.base.ACCControllerBase;
import jp.co.nissho_ele.acc.framework.base.ACCDialog;
import jp.co.nissho_ele.acc.framework.base.ACCDialog.Dialogs;
import jp.co.nissho_ele.acc.framework.base.ACCLookUp;
import jp.co.nissho_ele.acc.framework.base.ACCSessionManager;
import jp.co.nissho_ele.acc.framework.constants.ACCCodeConst;
import jp.co.nissho_ele.acc.framework.constants.ACCConst;
import jp.co.nissho_ele.acc.framework.constants.ACCDisplayIdConst;
import jp.co.nissho_ele.acc.framework.constants.ACCErrorConst;
import jp.co.nissho_ele.acc.framework.constants.ACCMessageIdConst;
import jp.co.nissho_ele.acc.framework.constants.ACCServiceIdConst;
import jp.co.nissho_ele.acc.framework.constants.ACCSessionKeyConst;
import jp.co.nissho_ele.acc.framework.utility.ACCCollectionUtility;
import jp.co.nissho_ele.acc.framework.utility.ACCRequestUtility;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004001_00_000Controller;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004004_00_000Model;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004005_00_000Model;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004006_00_000Model;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004007_00_000Model;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004008_00_000Model;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004009_00_000Model;
import jp.co.nissho_ele.acc.hm.hm1004.HM1004010_00_000Model;

/**
 * CL1015001_00_000Controller 基本検索画面
 *
 *--------------------------------------------------------------------------------------------*
 * History
 * Version	Date			Developer		Description
 *--------------------------------------------------------------------------------------------*
 * 1.0		2016/10/20		NEV-TuTD		First creation
 * 1.1		2016/12/20		NEV-KhoiBV 		Add business logic
 * 1.2      2018/08/15      ma              Update business logic
 * 1.3		2018/09/12		NEV-TungPD		課題No180対応：弁護士検索で検索して決定した弁護士が、基本検索画面に反映されないというバグ修正
 * 1.4		2018/10/24		JCBC-AN			転送先担当者名を取得する
 * 1.5		2018/10/24		LongNP			課題No271対応: 基本検索画面に「決定」ボタンが活性化するよう修正
 * 2.2		2019/07/03		miyazawa		ACC Ver2.2 No.3 法務検索タブを追加。検索画面から別顧客に切り替えるとき、法務基本画面ウインドウを閉じる。
 *                                          処理実績で決定ボタンを押したときの取得元ラベルが間違っていたのでついでに修正
 * 2.2		2019/07/01		miyazawa		ACC Ver2.2-02  転送情報テーブル検索が不要になったので削除
 * 2.2		2019/07/08		miyazawa		ACC Ver2.2-10 基本検索画面で前回の検索結果を保存する
 * 2.21     2019/10/28      NEV-SonNA       Fix kadai 498
 * 2.21		2020/01/30		KienNT			Fix [v2.21] Kadai 444
 *--------------------------------------------------------------------------------------------*
 */

@ManagedBean(name = "CL1015001_00_000Controller")
@ViewScoped
public class CL1015001_00_000Controller extends ACCControllerBase {

  /** Display ID */
  private static final String DISPLAY_ID = ACCDisplayIdConst.CL1015001_DISPLAY;

  /** KBN */
  private static final String CM9007001_KBN_1 = "1";

  /** 取得元に表示するラベル */
  private static final String OUT_SHUTOKUMOTO = "顧客基本検索";

  //v2.2 2019/07/08	miyazawa	[No.3] add start
  /** 処理実績検索で顧客を選択した場合の取得元ラベル */
  private static final String OUT_SHUTOKUMOTO_JISSEKI = "処理実績顧客検索";

  /** 法務検索で顧客を選択した場合の取得元ラベル */
  private static final String OUT_SHUTOKUMOTO_HOUMU = "法務検索";
  //v2.2 2019/07/08	miyazawa	[No.3] add end

  /** Search result flags */
  private static final int RESULT_OK = 0;
  private static final int RESULT_NOT_FOUND = 1;
  private static final int RESULT_MANY_RECORDS = 2;

  /** Control ID */
  private static final String BUTTON_KETTEI = "btnKettei";

  /** 基本検索画面フォームID */
  private static final String ID_FORM = "CL1015001Form";

  /** 基本検索画面決定ボタンID */
  private static final String ID_BUTTON_KETTEI = "CL1001001_MainTab:CL1015001Form:btnKettei";

  //v2.2 2019/07/02	miyazawa	[No.3] add start
  /** 基本検索タブのインデックス */
  private static final int TAB_INDEX_KIHON = 0;

  //v2.2 2019/07/02	miyazawa	[No.10] add start
  /** 基本検索データテーブルID */
  private static final String ID_DATA_TABLE = "CL1001001_MainTab:CL1015001Form:CL1015001Tab:CL1015001:datKihonKensakuKekka";
  //v2.2 2019/07/02	miyazawa	[No.10] add end
  /** 実績タブのインデックス */
  private static final int TAB_INDEX_JISSEKI = 1;

  /** 法務検索タブのインデックス */
  private static final int TAB_INDEX_HOUMU = 2;
  //v2.2 2019/07/02	miyazawa	[No.3] add end

  /** データBean */
  @ManagedProperty("#{CL1001012_00_000Model}")
  private CL1001012_00_000Model model012;

  /**
   * データBean
   */
  private CL1015001_00_000Model cl1015001Model;

  private List<CL1015002_00_000Model> dataFrame001;
  private CL1015002_00_000Model selectedRow;

  private int activeTabIndex;

  /**
   * Initialize
   * CL1015001_Event01
   */
  @PostConstruct
  public void init() {
    writeStartLog();

    setAuthority(DISPLAY_ID);
    setAuthority(ACCDisplayIdConst.CL1015003_DISPLAY);

    //v2.2 2019/07/02	miyazawa	[No.3] add start
    setAuthority(ACCDisplayIdConst.HM1004002_DISPLAY);
    //v2.2 2019/07/02	miyazawa	[No.3] add end

    cl1015001Model = new CL1015001_00_000Model();

    setControlStatus(DISPLAY_ID, BUTTON_KETTEI, false);

    //v2.2 2019/07/02	miyazawa	[No.10] update start
    //if (ACCConst.KADEN_MODE_JUDEN.equals(accBaseInfoModel.getKadenMode())
    //		&& ACCConst.CALL_STATUS_JUDEN.equals(accBaseInfoModel.getCallStatus())) {
    //	cl1015001Model.setDenwaBango(accBaseInfoModel.getKadenTel());
    //	// btnKensakuClick();
    //}
    displayInitData();
    //v2.2 2019/07/02	miyazawa	[No.10] update end

    writeEndLog();
  }

  /**
   * 検索ボタン押下
   * CL1015001_Event02
   */
  public void btnKensakuClick() {
    // 設定されている条件で検索を行う。
    writeStartLog();

    // 検索条件の入力チェック
    // 全て入力項目が空白
    if (cl1015001Model.isEmpty()) {
      //2020/02/17 AnPQ 課題　No522　START
      if(dataFrame001 != null){
        dataFrame001.clear();
      }
      //2020/02/17 AnPQ 課題　No522　END
      showDialogue(DISPLAY_ID, ACCMessageIdConst.COM_028);
      writeEndLog();
      return;
    }

    // 契約番号が9桁でない
    // show message required
    if (StringUtils.isNotEmpty(cl1015001Model.getKeiyakuBango())
        && cl1015001Model.getKeiyakuBango().length() < Integer.parseInt(accSystemParamUtility
        .getSystemParam(accBaseInfoModel.getCorpCd(), ACCCodeConst.SYS_CODE_SEARCH_001))) {
      accMessageUtility.showEmptyMessage(accBaseInfoModel.getCorpCd(), ACCMessageIdConst.CL1015001_01_003,
          ID_FORM);
      writeEndLog();
      return;
    }

    cl1015001Model.setKensakuKekka(ACCConst.STR_EMPTY);
    if (ACCCollectionUtility.isNotEmpty(dataFrame001)) {
      dataFrame001.clear();
    }
    selectedRow = null;
    setControlStatus(ACCDisplayIdConst.CL1015001_DISPLAY, BUTTON_KETTEI, false);


    // 検索結果表示最大件数
    // create baseInfo
    BaseInfo baseInfo = accBaseInfoUtility.getBaseInfo(ACCServiceIdConst.CL1015001_01_SERVICE, DISPLAY_ID);

    // create inBean
    CL1015001_01_000InBean inBean = new CL1015001_01_000InBean();
    inBean.setBaseInfo(baseInfo);
    inBean.setCustomerNo(cl1015001Model.getKokyakuBango());
    inBean.setKyKeiyakuNo(cl1015001Model.getKeiyakuBango());
    inBean.setCardNo(cl1015001Model.getKaiinBango());
    inBean.setNameKana(cl1015001Model.getShimeiKana());
    inBean.setNameKanji(cl1015001Model.getShimeiKanji());

    if (StringUtils.isNotEmpty(cl1015001Model.getSeinengappi())) {
      inBean.setBirthday(cl1015001Model.getSeinengappi());
    }

    inBean.setTelNo(cl1015001Model.getDenwaBango());
    inBean.setTtName(cl1015001Model.getTantoshamei());
    inBean.setKinmusakiKana(cl1015001Model.getKimmusakiKana());
    inBean.setLawyerNameKana(cl1015001Model.getBengoshiKana());
    inBean.setLawyerNameKanji(cl1015001Model.getBengoshiKanji());
    inBean.setLawyerOffice(cl1015001Model.getBengoshiJimusho());
    //2020/01/30 KienNT Fix [v2.21] Kadai 444 START
    //inBean.setAddressCd(cl1015001Model.getJushoKodo());
    //inBean.setAreaCd(cl1015001Model.getEriaKodo());
    //2020/01/30 KienNT Fix [v2.21] Kadai 444 END
    inBean.setAddressZipCd(cl1015001Model.getJitakuYubinBango());

    // create outBean
    CL1015001_01_000OutBean outBean = (CL1015001_01_000OutBean) ACCLookUp.execProcess(inBean);

    // check service success or not
    if (!ACCConst.NORMAL.equals(outBean.getErrorFlg())
        || !ACCErrorConst.SUCCESS_CODE.equals(outBean.getErrorInfo().getCode())) {
      // EJBエラーの場合
      writeErrorLog(outBean.getErrorInfo().getMsgDetail());
      showDialogueError(DISPLAY_ID, outBean.getErrorInfo());
      return;
    }
    // clear sort
    UIComponent dt = FacesContext.getCurrentInstance().getViewRoot()
        .findComponent("CL1001001_MainTab:CL1015001Form:CL1015001Tab:CL1015001:datKihonKensakuKekka");
    if (dt != null) {
      dt.setValueExpression("sortBy", null);
    }

    switch (outBean.getResultFlg()) {
      case RESULT_OK: // OK
        searchData(outBean.getRowNumber());
        break;

      case RESULT_NOT_FOUND:
        dataFrame001 = new ArrayList<>();
        cl1015001Model.setKensakuKekka(ACCConst.STR_EMPTY);

        // 2-5の処理にて取得した件数が0件の場合、顧客登録画面（CL5001001）をポップアップする。
        ACCDialog.openDialog(Dialogs.CL5001001);
        break;

      case RESULT_MANY_RECORDS:
        // 2-4で取得の抽出件数 ＞ 2-2で取得の検索結果表示最大件数 (resultFlg = 2)
        // エラーメッセージを出力し、処理を終了する
        showDialogue(DISPLAY_ID, ACCMessageIdConst.COM_029, (Runnable) null, null,
            accSystemParamUtility.getSystemParam(baseInfo.getCorpCd(), ACCCodeConst.SYS_CODE_SONOTA_002));
        return;
    }

    // 2019/07/09 SonNA Fix [2.12] Kadai 508 Start
    // firtPage when click button Search
    DataTable table = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
        .findComponent("CL1001001_MainTab:CL1015001Form:CL1015001Tab:CL1015001:datKihonKensakuKekka");
    if (table != null) {
      table.setFirst(0);
    }
    // scroll table
    ACCRequestUtility.executeScript("scrollTableCL1015001();");
    ACCRequestUtility.executeScript("scrollTopCL1015001();");
    // 2019/07/09 SonNA Fix [2.12] Kadai 508 End
  }

  /**
   * 入力クリアボタン押下
   * CL1015001_Event03
   */
  public void btnNyuryokuKuriaClick() {
    // 代理人検索画面を表示
    writeStartLog();

    cl1015001Model.clear();

    writeEndLog();
  }

  /**
   * 弁護士検索ボタン押下
   * CL1015001_Event04
   */
  public void btnBengoshiKensakuClick() {
    writeStartLog();

    ACCSessionManager.setSession(ACCSessionKeyConst.BEAR_OR_JUST, ACCConst.BEAR);

    // 代理人検索画面を表示
    ACCDialog.openDialog(Dialogs.CL5009001);

    writeEndLog();
  }

  /**
   * Return from CL5009001 screen
   */
  public void dialogReturnFromCL5009(SelectEvent event) {
    writeStartLog();
    String mode = (String) event.getObject();
    if (ACCConst.MODE_CLOSE_ABNORMAL.equals(mode)) {
      cl1015001Model.setBengoshiKana(
          (String) ACCSessionManager.getSession(ACCSessionKeyConst.CL5009001_LAWYER_NAME_KANA));
      cl1015001Model.setBengoshiKanji(
          (String) ACCSessionManager.getSession(ACCSessionKeyConst.CL5009001_LAWYER_NAME_KANJI));
      cl1015001Model.setBengoshiJimusho(
          (String) ACCSessionManager.getSession(ACCSessionKeyConst.CL5009001_LAWYER_OFFICE_KANJI));
    }

    clearSession();

    writeEndLog();
  }

  /**
   * Clear session after use
   */
  private void clearSession() {
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_CODE);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_NAME_KANA);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_NAME_KANJI);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_TYPE);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_OFFICE_KANJI);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_ZIP);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_ADDR_KANJI1);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_ADDR_KANJI2);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_ADDR_KANJI3);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_TEL_NO);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL5009001_LAWYER_FAX_NO);
  }

  /**
   * Event when select row on grid
   */
  public void rowSelect() {
    writeStartLog();

    // Enable button
    if (selectedRow != null) {
      setControlStatus(ACCDisplayIdConst.CL1015001_DISPLAY, BUTTON_KETTEI, true);
    } else {
      setControlStatus(ACCDisplayIdConst.CL1015001_DISPLAY, BUTTON_KETTEI, false);
    }
    ACCRequestUtility.updateComponents(ID_BUTTON_KETTEI);

    writeEndLog();
  }

  /**
   * Event change tab
   */
  public void onTabChange(TabChangeEvent event) {
    writeStartLog();

    //v2.2 2019/07/02	miyazawa	[No.3] update start
    //activeTabIndex = ((TabView) event.getComponent()).getActiveIndex();

    //if (activeTabIndex == 0) {
    //	rowSelect();
    //} else {
    //	CL1015003_00_000Controller cl1015003 = ACCRequestUtility.getManagedBean(CL1015003_00_000Controller.class);
    //	if (cl1015003.getSelectedRow() != null) {
    //		selectRowClick(1);
    //	} else {
    //		selectRowClick(0);
    //	}
    //}

    switch(activeTabIndex){
      case TAB_INDEX_KIHON:
        rowSelect();
        break;

      case TAB_INDEX_JISSEKI:
        CL1015003_00_000Controller cl1015003 = ACCRequestUtility.getManagedBean(CL1015003_00_000Controller.class);
        if (cl1015003.getSelectedRow() != null) {
          selectRowClick(1);
        } else {
          selectRowClick(0);
        }
        break;

      case TAB_INDEX_HOUMU:
        HM1004001_00_000Controller hm1004001 = ACCRequestUtility.getManagedBean(HM1004001_00_000Controller.class);
        changeKetteiButtonActiveState( hm1004001.getSeledtedRow() != null );
        break;
    }

    // 2020/02/18 TuyenNT Fix [v2.21]Kadai17 START
    ACCRequestUtility.executeScript("window.cl1001.setTabIndexLinkPage()");
    // 2020/02/18 TuyenNT Fix [v2.21]Kadai17 END
    //v2.2 2019/07/02	miyazawa	[No.3] update end

    writeEndLog();
  }

  /**
   * Check row selected in tab2
   */
  public void selectRowClick(int outIdTab) {
    writeStartLog();

    //v2.2 2019/07/02	miyazawa	[No.3] update start
    //if (outIdTab == 1) {
    //	setControlStatus(ACCDisplayIdConst.CL1015001_DISPLAY, BUTTON_KETTEI, true);
    //} else {
    //	setControlStatus(ACCDisplayIdConst.CL1015001_DISPLAY, BUTTON_KETTEI, false);
    //}
    //ACCRequestUtility.updateComponents(ID_BUTTON_KETTEI);
    changeKetteiButtonActiveState(outIdTab == 1);
    //v2.2 2019/07/02	miyazawa	[No.3] update end
    writeEndLog();
  }

  //v2.2 2019/07/02	miyazawa	[No.10] add start
  /**
   * 決定ボタンの活性/非活性を変更する
   * @param enabled true->enabled  false->disabled
   */
  public void changeKetteiButtonActiveState(boolean enabled){
    setControlStatus(ACCDisplayIdConst.CL1015001_DISPLAY, BUTTON_KETTEI, enabled);
    ACCRequestUtility.updateComponents(ID_BUTTON_KETTEI);
  }
  //v2.2 2019/07/02	miyazawa	[No.10] add end


  /**
   * グリッド上の項目をダブルクリック
   * CL1015001_Event06
   */
  public void datKihonKensakuKekkaDoubleClick() {
    // 選択した項目で、督促基本画面を表示する。
    writeStartLog();

    if (selectedRow != null) {
      dblClickRow(selectedRow.getKokyakuBango(), OUT_SHUTOKUMOTO);
    }

    writeEndLog();
  }


  /**
   * グリッド上の項目をダブルクリックした時のイベント
   * @param customerNo ダブルクリックした対象の顧客番号
   * @param outShuToKuMoTo 顧客情報を検索した画面
   */
  public void dblClickRow(String customerNo, String outShuToKuMoTo) {
    writeStartLog();

    if (StringUtils.isEmpty(customerNo)) {
      return;
    }

    // Lock customer exclusively
    // create baseInfo
    BaseInfo baseInfo = accBaseInfoUtility.getBaseInfo(ACCServiceIdConst.CM9007001_01_SERVICE,
        ACCDisplayIdConst.CL1015001_DISPLAY);

    // create inBean
    CM9007001_01_000InBean inBean = new CM9007001_01_000InBean();
    inBean.setBaseInfo(baseInfo);
    inBean.setCustomerNoNew(customerNo);
    inBean.setJobId(ACCServiceIdConst.CL1015001_00_SERVICE);
    inBean.setSubJobId(ACCServiceIdConst.CM9007001_01_SERVICE);
    inBean.setKbn(CM9007001_KBN_1);

    // create outBean
    CM9007001_01_000OutBean outBean = (CM9007001_01_000OutBean) ACCLookUp.execProcess(inBean);

    // check service success or not
    // 2019/10/28 SonNA Fix [v2.21]kadai498 START
    if (!ACCConst.NORMAL.equals(outBean.getErrorFlg())) {
      // EJBエラーの場合
      writeErrorLog(outBean.getErrorInfo().getMsgDetail());
      // show message error
      showDialogueError(ACCDisplayIdConst.CL1015001_DISPLAY, outBean.getErrorInfo());
      writeEndLog();
      return;
    } else if (!ACCErrorConst.SUCCESS_CODE.equals(outBean.getErrorInfo().getCode())) {
      // EJBエラーの場合
      /*writeErrorLog(outBean.getErrorInfo().getMsgDetail());*/

      // check message code
      if (ACCMessageIdConst.COM_027.equals(outBean.getErrorInfo().getCode())) {

        Runnable run = new Runnable() {
          @Override
          public void run() {
            cm9007001_02_000serviceHandle(customerNo, outShuToKuMoTo);
          }
        };
        showDialogue(ACCDisplayIdConst.CL1015001_DISPLAY, ACCMessageIdConst.COM_027, null, run, (Runnable) null,
            outBean.getErrorInfo().getAddMsg1());
        writeEndLog();
        return;
      }
    }
    // 2019/10/28 SonNA Fix [v2.21]kadai498 END
    // 顧客転送済みチェック
    // create baseInfo
    BaseInfo baseInfo1 = accBaseInfoUtility.getBaseInfo(ACCServiceIdConst.CL1028001_03_SERVICE,
        ACCDisplayIdConst.CL1015001_DISPLAY);
    // init inBean
    CL1028001_03_000InBean inBean1 = new CL1028001_03_000InBean();
    inBean1.setCustomerNo(cl1015001Model.getKokyakuBango());
    inBean1.setBaseInfo(baseInfo1);
    // out Bean
    CL1028001_03_000OutBean outBean1 = (CL1028001_03_000OutBean) ACCLookUp.execProcess(inBean1);
    // check service success or not
    if (!ACCConst.NORMAL.equals(outBean1.getErrorFlg())
        || !ACCErrorConst.SUCCESS_CODE.equals(outBean1.getErrorInfo().getCode())) {
      writeErrorLog(outBean1.getErrorInfo().getMsgDetail());
      this.showDialogueError(ACCDisplayIdConst.CL1028001_DISPLAY, outBean1.getErrorInfo());
      return;
    }

    //v2.2 2019/07/02	miyazawa	[No.3] add start
    closeHoumuKihonWindows(customerNo);
    //v2.2 2019/07/02	miyazawa	[No.3] add end


    //String name = (String) ACCSessionManager.getSession(ACCSessionKeyConst.CL1028001_SHIMEI_KANJI);
    // check exits customerNo
    if (!ACCStringUtil.isEmpty(outBean1.getTtcd())) {
      this.showDialogue(ACCDisplayIdConst.CL1015001_DISPLAY, ACCMessageIdConst.COM_058, () -> {
        // setting reference mode
        accBaseInfoModel.setReferenceMode(true);
        accBaseInfoModel.setCustomerNo(cl1015001Model.getKokyakuBango());
        // remove all tab
        CL1001001_00_000Controller cl1001 = ACCRequestUtility.getManagedBean(CL1001001_00_000Controller.class);
        cl1001.removeTabAll();
      }, null, outBean1.getTtname());
      return;
    }
    accBaseInfoModel.setReferenceMode(false);
    // set session
    accBaseInfoModel.setCustomerNo(customerNo);
    model012.setCustomerInfo(outShuToKuMoTo);
    model012.setMessageInfo(ACCConst.FULLSIZE_SPACE);

    //v2.2 2019/07/02	miyazawa	[No.10] add start
    accBaseInfoModel.setLastBasicSearchSelectedRow(selectedRow);
    //v2.2 2019/07/02	miyazawa	[No.10] add end

    // close screen
    close();

    writeEndLog();
  }

  //v2.2 2019/07/02	miyazawa	[No.3] add start
  /**
   * 現在子表示している顧客とは別の顧客に切り替えるとき、全ての法務基本別ウインドウを閉じる
   * @param customerNo 画面で選択した顧客番号
   */
  private void closeHoumuKihonWindows(String customerNo){
    if(accBaseInfoModel.getCustomerNo().equals(customerNo)){
      return;
    }
    ACCRequestUtility.executeScript("closeHoumuKihonWindows()");
  }
  //v2.2 2019/07/02	miyazawa	[No.3] add end

  //v2.2 2019/07/02	miyazawa	[No.10] add start
  /**
   * データの初期表示。どの理由で基本検索画面を開いたかによって異なる。
   */
  private void displayInitData(){

    //受電した時に基本検索画面を表示した時は電話番号だけ設定する
    if(ACCConst.CALL_STATUS_JUDEN.equals(accBaseInfoModel.getCallStatus())){
      switch (accBaseInfoModel.getKadenMode()){
        case ACCConst.KADEN_MODE_PV:
        case ACCConst.KADEN_MODE_JUDEN:
          cl1015001Model.setDenwaBango(accBaseInfoModel.getKadenTel());
      }
      return ;
    }

    //督促基本からの通常使用時は前回検索条件を復元する
    if(accBaseInfoModel.getLastBasicSearchCondition() != null){
      restoreLastSituation();
      return;
    }
  }

  /**
   * 前回検索時の条件、検索結果、選択行を復元する
   */
  private void restoreLastSituation(){

    cl1015001Model = accBaseInfoModel.getLastBasicSearchCondition();

    selectedRow = accBaseInfoModel.getLastBasicSearchSelectedRow();

    if(selectedRow != null){
      changeKetteiButtonActiveState(true);
    }

    dataFrame001 = accBaseInfoModel.getLastBasicSearchResult();

    //ページ選択
    int pageIndex = (dataFrame001.indexOf(selectedRow)/15) * 15;
    DataTable dataTable = (DataTable) FacesContext.getCurrentInstance().getViewRoot()
        .findComponent(ID_DATA_TABLE);
    dataTable.setFirst(pageIndex);
  }
  //v2.2 2019/07/02	miyazawa	[No.10] add end


  /**
   * 顧客排他ロック解除
   */
  public void cm9007001_02_000serviceHandle(String customerNo, String outShuToKuMoTo) {
    writeStartLog();
    BaseInfo baseInfo = accBaseInfoUtility.getBaseInfo(ACCServiceIdConst.CM9007001_02_SERVICE,
        ACCDisplayIdConst.CL1015001_DISPLAY);

    // create inBean
    CM9007001_02_000InBean inBean = new CM9007001_02_000InBean();
    inBean.setBaseInfo(baseInfo);
    inBean.setJobId(ACCDisplayIdConst.CL1015001_DISPLAY);

    // create outBean
    CM9007001_02_000OutBean outBean = (CM9007001_02_000OutBean) ACCLookUp.execProcess(inBean);

    // check service success or not
    if (!ACCConst.NORMAL.equals(outBean.getErrorFlg())
        || !ACCErrorConst.SUCCESS_CODE.equals(outBean.getErrorInfo().getCode())) {
      // EJBエラーの場合
      writeErrorLog(outBean.getErrorInfo().getMsgDetail());
      // show message error
      showDialogueError(ACCDisplayIdConst.CL1015001_DISPLAY, outBean.getErrorInfo());
      return;
    }

    //v2.2 2019/07/02	miyazawa	[No.3] add start
    closeHoumuKihonWindows(customerNo);
    //v2.2 2019/07/02	miyazawa	[No.3] add end

    // setting Reference Mode
    accBaseInfoModel.setReferenceMode(true);
    accBaseInfoModel.setCustomerNo(customerNo);
    model012.setCustomerInfo(outShuToKuMoTo);
    model012.setMessageInfo(ACCConst.FULLSIZE_SPACE);

    // close screen
    CL1001001_00_000Controller cl1001 = ACCRequestUtility.getManagedBean(CL1001001_00_000Controller.class);
    cl1001.removeTab(TabData.CL1015001);
    writeEndLog();
  }

  /**
   * 決定ボタン押下
   * CL1015001_Event08
   */
  public void btnKetteiClick() {
    // 選択した項目で、督促基本画面を表示する。
    writeStartLog();

    //v2.2 2019/07/02	miyazawa	[No.3] update start
    //String customerNo = null;
    //if (activeTabIndex == 0) {
    //	// 基本検索
    //	if (selectedRow != null) {
    //		customerNo = selectedRow.getKokyakuBango();
    //	}
    //} else {
    //	// 処理実績
    //	customerNo = ACCRequestUtility.getManagedBean(CL1015003_00_000Controller.class).getSelectedCustomerNo();
    //}

    //if (StringUtils.isNotEmpty(customerNo)) {
    //dblClickRow(customerNo, OUT_SHUTOKUMOTO);
    //}

    String customerNo = null;
    String sourceLabel = null;
    switch( activeTabIndex ){

      case TAB_INDEX_KIHON:
        if (selectedRow != null) {
          customerNo = selectedRow.getKokyakuBango();
          sourceLabel = OUT_SHUTOKUMOTO;
        }
        break;

      case TAB_INDEX_JISSEKI:
        customerNo = ACCRequestUtility.getManagedBean(CL1015003_00_000Controller.class).getSelectedCustomerNo();
        sourceLabel = OUT_SHUTOKUMOTO_JISSEKI;
        break;

      case TAB_INDEX_HOUMU:
        customerNo = ACCRequestUtility.getManagedBean(HM1004001_00_000Controller.class).getSelectedCustomerNo();
        sourceLabel = OUT_SHUTOKUMOTO_HOUMU;
        break;

      default:
        writeEndLog();
        return;
    }

    if(ACCStringUtil.isEmpty(customerNo)){
      writeEndLog();
      return;
    }

    dblClickRow(customerNo, sourceLabel);

    //v2.2 2019/07/02	miyazawa	[No.3] update end

    writeEndLog();
  }

  /**
   * キャンセルボタン押下
   * CL1015001_Event09
   */
  public void btnKyanseruClick() {
    writeStartLog();

    // 画面を閉じる。
    close();

    writeEndLog();
  }

  /**
   * search data
   * @param records : the number of customers in searched result
   */
  private void searchData(int rowNumber) {

    // create baseInfo
    BaseInfo baseInfo = accBaseInfoUtility.getBaseInfo(ACCServiceIdConst.CL1015001_02_SERVICE, DISPLAY_ID);

    // create inBean
    CL1015001_02_000InBean inBean = new CL1015001_02_000InBean();
    inBean.setBaseInfo(baseInfo);

    // create outBean
    CL1015001_02_000OutBean outBean = (CL1015001_02_000OutBean) ACCLookUp.execProcess(inBean);

    // check service success or not
    if (!ACCConst.NORMAL.equals(outBean.getErrorFlg())
        || !ACCErrorConst.SUCCESS_CODE.equals(outBean.getErrorInfo().getCode())) {
      writeErrorLog(outBean.getErrorInfo().getMsgDetail());
      showDialogueError(DISPLAY_ID, outBean.getErrorInfo());

      return;
    }

    dataFrame001 = new ArrayList<>();
    outBean.getResultList().forEach(dto -> dataFrame001.add(convertDto2Model(dto)));
    //v2.2 2019/07/02	miyazawa	[No.10] add start
    cl1015001Model.setKensakuKekka(String.valueOf(rowNumber));

    accBaseInfoModel.setLastBasicSearchCondition(cl1015001Model);
    accBaseInfoModel.setLastBasicSearchResult(dataFrame001);
    accBaseInfoModel.setLastBasicSearchSelectedRow(null);
    //v2.2 2019/07/02	miyazawa	[No.10] add end

    // Check the number record of result
    switch (rowNumber) {
      case 0:
        cl1015001Model.setKensakuKekka(ACCConst.STR_EMPTY);

        // 2-5の処理にて取得した件数が0件の場合、顧客登録画面（CL5001001）をポップアップする。
        ACCDialog.openDialog(Dialogs.CL5001001);
        break;

      case 1:
        //v2.2 2019/07/08	miyazawa	[No.3] add start
        selectedRow = dataFrame001.get(0);
        //v2.2 2019/07/08	miyazawa	[No.3] add end
        dblClickRow(dataFrame001.get(0).getKokyakuBango(), OUT_SHUTOKUMOTO);
        break;

      //v2.2 2019/07/02	miyazawa	[No.10] add delete
      //default:
      // Output search result to screen
      //cl1015001Model.setKensakuKekka(String.valueOf(rowNumber));
      //break;
      //v2.2 2019/07/02	miyazawa	[No.10] add delete
    }

  }

  /**
   * Convert DTO to Model
   *
   * @param dto
   * @return
   */
  private CL1015002_00_000Model convertDto2Model(CL1015001_02_000Rselect1Dto dto) {
    CL1015002_00_000Model record = new CL1015002_00_000Model();
    record.setBunruiMeisho(dto.getBnname());
    record.setKeiyakuSt(dto.getKyStatusRyaku());
    record.setShimeiKanji(dto.getKjNameKanji());
    record.setShimeiKana(dto.getKjNameKana());
    record.setSeinengappi(dto.getKjTanjyoDate());
    record.setJitakuDenwaBango(dto.getKjJTelNo());
    record.setYubinBango(dto.getKjJJzipcd());
    record.setJitakuJusho(dto.getKjJJkanji());
    record.setTantosha(dto.getTtName());
    record.setKokyakuBango(dto.getCustomerNo());
    record.setKaiinBango(dto.getKyHyojiyoKaiinNo());
    record.setKimmusakiMei(dto.getKjKinmusakiKanji());
    record.setKimmusakiDenwaBango(dto.getKjRTelNo());
    record.setEntaiTsukisu(dto.getKyEntaiMCnt());
    record.setBengoshiJimushoMei(dto.getLawyerOfficeKanji());
    record.setBunno(dto.getBunno());
    record.setKeiyakuNo(dto.getKyKeiyakuNo());

    return record;
  }

  public void backFromPopUp(SelectEvent event) {

    writeStartLog();
    // close all tab
    CL1001001_00_000Controller controller001 = ACCRequestUtility.getManagedBean(CL1001001_00_000Controller.class);
    // update old customerNo value
    if (!ACCSessionManager.containsKey(ACCSessionKeyConst.CL1001001_OLD_CUSTOMER_NO)) {
      ACCSessionManager.setSession(ACCSessionKeyConst.CL1001001_OLD_CUSTOMER_NO, "");
    }
    String oldCustomerNo = (String) ACCSessionManager.getSession(ACCSessionKeyConst.CL1001001_OLD_CUSTOMER_NO);
    if (!Objects.equals(accBaseInfoModel.getCustomerNo(), oldCustomerNo)) {
      controller001.removeTabAll();
    }
    writeEndLog();
  }

  /**
   * Close
   */
  private void close() {
    // Delete session
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL1015001_INBANDO_KIHON_KENSAKU_FLG);
    ACCSessionManager.deleteSession(ACCSessionKeyConst.CL1015001_PHONE_NO);

    // Close tab
    ACCRequestUtility.getManagedBean(CL1001001_00_000Controller.class).removeTab(TabData.CL1015001);
  }

  /**
   * @return the cl1015001Model
   */
  public CL1015001_00_000Model getCl1015001Model() {
    return cl1015001Model;
  }

  /**
   * @param cl1015001Model
   *            the cl1015001Model to set
   */
  public void setCl1015001Model(CL1015001_00_000Model cl1015001Model) {
    this.cl1015001Model = cl1015001Model;
  }

  /**
   * @return the dataFrame001
   */
  public List<CL1015002_00_000Model> getDataFrame001() {
    return dataFrame001;
  }

  /**
   * @param dataFrame001
   *            the dataFrame001 to set
   */
  public void setDataFrame001(List<CL1015002_00_000Model> dataFrame001) {
    this.dataFrame001 = dataFrame001;
  }

  /**
   * Get selectedRow
   * @return selectedRow
   */
  public CL1015002_00_000Model getSelectedRow() {
    return selectedRow;
  }

  /**
   * Set selectedRow
   * @param selectedRow
   */
  public void setSelectedRow(CL1015002_00_000Model selectedRow) {
    this.selectedRow = selectedRow;
  }

  /**
   * Get activeTabIndex
   * @return activeTabIndex
   */
  public int getActiveTabIndex() {
    return activeTabIndex;
  }

  /**
   * Set activeTabIndex
   * @param activeTabIndex
   */
  public void setActiveTabIndex(int activeTabIndex) {
    this.activeTabIndex = activeTabIndex;
  }

  /**
   * Get model012
   * @return model012
   */
  public CL1001012_00_000Model getModel012() {
    return model012;
  }

  /**
   * Set model012
   * @param model012
   */
  public void setModel012(CL1001012_00_000Model model012) {
    this.model012 = model012;
  }

}