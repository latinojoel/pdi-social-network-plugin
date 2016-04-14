package com.latinojoel.di.trans.steps.socialnetwork.fb.postmsg;

import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Shell;
import org.pentaho.di.core.CheckResultInterface;
import org.pentaho.di.core.Const;
import org.pentaho.di.core.Counter;
import org.pentaho.di.core.annotations.Step;
import org.pentaho.di.core.database.DatabaseMeta;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.exception.KettleStepException;
import org.pentaho.di.core.exception.KettleXMLException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.core.variables.VariableSpace;
import org.pentaho.di.core.xml.XMLHandler;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.repository.ObjectId;
import org.pentaho.di.repository.Repository;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;
import org.w3c.dom.Node;

import com.latinojoel.di.ui.trans.steps.socialnetwork.PostMsgDialog;

/**
 * This class is responsible for implementing functionality regarding step meta. All Kettle steps
 * have an extension of this where private fields have been added with public accessors.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
@Step(id = "FacebookPostMsg", name = "FacebookPostMsg.Step.Name",
    description = "FacebookPostMsg.Step.Description",
    categoryDescription = "FacebookPostMsg.Step.Category",
    image = "com/latinojoel/di/trans/steps/socialnetwork/facebook.png",
    i18nPackageName = "com.latinojoel.di.trans.steps.socialnetwork",
    casesUrl = "https://github.com/ivylabs", documentationUrl = "https://github.com/ivylabs",
    forumUrl = "https://github.com/ivylabs")
public class PostMsgMeta extends BaseStepMeta implements StepMetaInterface {

  /** for i18n purposes. **/
  private static final Class<?> PKG = PostMsgMeta.class;

  /** App ID. */
  private String appId;

  /** App secret. */
  private String appSecret;

  /** Token. */
  private String token;

  /** Permissions. */
  private String permissions;

  /** Message field. */
  private String messageField;


  public String getAppId() {
    return appId;
  }

  public void setAppId(String appId) {
    this.appId = appId;
  }

  public String getAppSecret() {
    return appSecret;
  }

  public void setAppSecret(String appSecret) {
    this.appSecret = appSecret;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getPermissions() {
    return permissions;
  }

  public void setPermissions(String permissions) {
    this.permissions = permissions;
  }

  public String getMessageField() {
    return messageField;
  }

  public void setMessageField(String messageField) {
    this.messageField = messageField;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public String getXML() {
    final StringBuilder retval = new StringBuilder();

    retval.append("    " + XMLHandler.addTagValue("appId", appId));
    retval.append("    " + XMLHandler.addTagValue("appSecret", appSecret));
    retval.append("    " + XMLHandler.addTagValue("token", token));
    retval.append("    " + XMLHandler.addTagValue("permissions", permissions));
    retval.append("    " + XMLHandler.addTagValue("messageField", messageField));

    return retval.toString();
  }

  /**
   * Reads data from XML transformation file.
   * 
   * @param stepnode the step XML node.
   * @throws KettleXMLException the kettle XML exception.
   */
  public void readData(Node stepnode) throws KettleXMLException {
    try {

      appId = XMLHandler.getTagValue(stepnode, "appId");
      appSecret = XMLHandler.getTagValue(stepnode, "appSecret");
      token = Const.NVL(XMLHandler.getTagValue(stepnode, "token"), "");
      permissions = XMLHandler.getTagValue(stepnode, "permissions");
      messageField = XMLHandler.getTagValue(stepnode, "messageField");

    } catch (Exception e) {
      throw new KettleXMLException(BaseMessages.getString(PKG,
          "FacebookNewsFeeds.Exception.UnexpectedErrorInReadingStepInfo"), e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleException
   */
  @Override
  public void readRep(Repository rep, ObjectId idStep,
      List<DatabaseMeta> databases, Map<String, Counter> counters)
      throws KettleException {
    try {

      appId = rep.getStepAttributeString(idStep, "appId");
      appSecret = rep.getStepAttributeString(idStep, "appSecret");
      token = rep.getStepAttributeString(idStep, "token");
      permissions = rep.getStepAttributeString(idStep, "permissions");
      messageField = rep.getStepAttributeString(idStep, "messageField");

    } catch (Exception e) {
      throw new KettleException(BaseMessages.getString(PKG,
          "FacebookNewsFeeds.Exception.UnexpectedErrorInReadingStepInfo"), e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleException
   */
  @Override
  public void saveRep(Repository rep, ObjectId idTransformation,
      ObjectId idStep) throws KettleException {
    try {

      rep.saveStepAttribute(idTransformation, idStep, "appId",
          this.appId);
      rep.saveStepAttribute(idTransformation, idStep, "appSecret",
          appSecret);
      rep.saveStepAttribute(idTransformation, idStep, "token",
          token);
      rep.saveStepAttribute(idTransformation, idStep, "permissions",
          permissions);
      rep.saveStepAttribute(idTransformation, idStep, "messageField",
          messageField);

    } catch (Exception e) {
      throw new KettleException(BaseMessages.getString(PKG,
          "FacebookNewsFeeds.Exception.UnableToSaveStepInfoToRepository") + idStep, e);
    }
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleStepException
   */
  @Override
  public void getFields(RowMetaInterface r, String origin, RowMetaInterface[] info,
      StepMeta nextStep, VariableSpace space) throws KettleStepException {
    return;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public Object clone() {
    return super.clone();
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleXMLException
   */
  @Override
  public void loadXML(Node stepnode, List<DatabaseMeta> databases, Map<String, Counter> counters)
      throws KettleXMLException {
    readData(stepnode);
  }

  /**
   * Sets the default values.
   */
  public void setDefault() {}

  /**
   * {@inheritDoc}
   */
  @Override
  public void check(List<CheckResultInterface> remarks, TransMeta transmeta, StepMeta stepMeta,
      RowMetaInterface prev, String[] input, String[] output, RowMetaInterface info) {}

  /**
   * Get the Step dialog, needs for configure the step.
   * 
   * @param shell the shell.
   * @param meta the associated base step metadata.
   * @param transMeta the associated transformation metadata.
   * @param name the step name
   * @return The appropriate StepDialogInterface class.
   */
  public StepDialogInterface getDialog(Shell shell, StepMetaInterface meta,
      TransMeta transMeta, String name) {
    return new PostMsgDialog(shell, (BaseStepMeta) meta, transMeta, name);
  }

  /**
   * Get the executing step, needed by Trans to launch a step.
   * 
   * @param stepMeta The step info.
   * @param stepDataInterface the step data interface linked to this step. Here the step can store
   *        temporary data, database connections, etc.
   * @param cnr The copy nr to get.
   * @param transMeta The transformation info.
   * @param disp The launching transformation.
   * @return The appropriate StepInterface class.
   */
  public StepInterface getStep(StepMeta stepMeta,
      StepDataInterface stepDataInterface, int cnr, TransMeta transMeta,
      Trans disp) {
    return new PostMsg(stepMeta, stepDataInterface, cnr, transMeta, disp);
  }

  /**
   * Get a new instance of the appropriate data class. This data class implements the
   * StepDataInterface. It basically contains the persisting data that needs to live on, even if a
   * worker thread is terminated.
   * 
   * @return The appropriate StepDataInterface class.
   */
  public StepDataInterface getStepData() {
    return new PostMsgData();
  }

}
