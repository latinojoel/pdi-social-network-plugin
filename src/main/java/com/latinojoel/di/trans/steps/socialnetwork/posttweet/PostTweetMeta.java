package com.latinojoel.di.trans.steps.socialnetwork.posttweet;

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

import com.latinojoel.di.ui.trans.steps.socialnetwork.PostTweetDialog;

/**
 * This class is responsible for implementing functionality regarding step meta. All Kettle steps
 * have an extension of this where private fields have been added with public accessors.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
@Step(id = "PostTweet", name = "PostTweet.Step.Name",
    description = "PostTweet.Step.Description",
    categoryDescription = "PostTweet.Step.Category",
    image = "com/latinojoel/di/trans/steps/socialnetwork/twitter.png",
    i18nPackageName = "com.latinojoel.di.trans.steps.socialnetwork",
    casesUrl = "https://github.com/ivylabs", documentationUrl = "https://github.com/ivylabs",
    forumUrl = "https://github.com/ivylabs")
public class PostTweetMeta extends BaseStepMeta implements
    StepMetaInterface {

  /** for i18n purposes. **/
  private static final Class<?> PKG = PostTweetMeta.class;

  /** Consumer key. */
  private String consumerKey;

  /** Consumer secret. */
  private String consumerSecret;

  /** Token. */
  private String token;

  /** Secret. */
  private String secret;

  /** Message field. */
  private String messageField;


  public String getConsumerKey() {
    return consumerKey;
  }

  public void setConsumerKey(String consumerKey) {
    this.consumerKey = consumerKey;
  }

  public String getConsumerSecret() {
    return consumerSecret;
  }

  public void setConsumerSecret(String consumerSecret) {
    this.consumerSecret = consumerSecret;
  }

  public String getToken() {
    return token;
  }

  public void setToken(String token) {
    this.token = token;
  }

  public String getSecret() {
    return secret;
  }

  public void setSecret(String secret) {
    this.secret = secret;
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

    retval.append("    " + XMLHandler.addTagValue("consumerKey", consumerKey));
    retval.append("    " + XMLHandler.addTagValue("consumerSecret", consumerSecret));
    retval.append("    " + XMLHandler.addTagValue("token", token));
    retval.append("    " + XMLHandler.addTagValue("secret", secret));
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

      consumerKey = XMLHandler.getTagValue(stepnode, "consumerKey");
      consumerSecret = XMLHandler.getTagValue(stepnode, "consumerSecret");
      token = Const.NVL(XMLHandler.getTagValue(stepnode, "token"), "");
      secret = XMLHandler.getTagValue(stepnode, "secret");
      messageField = XMLHandler.getTagValue(stepnode, "messageField");

    } catch (Exception e) {
      throw new KettleXMLException(BaseMessages.getString(PKG,
          "TwitterStreamInput.Exception.UnexpectedErrorInReadingStepInfo"), e);
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

      consumerKey = rep.getStepAttributeString(idStep, "consumerKey");
      consumerSecret = rep.getStepAttributeString(idStep, "consumerSecret");
      token = rep.getStepAttributeString(idStep, "token");
      secret = rep.getStepAttributeString(idStep, "secret");
      messageField = rep.getStepAttributeString(idStep, "messageField");

    } catch (Exception e) {
      throw new KettleException(BaseMessages.getString(PKG,
          "TwitterStreamInput.Exception.UnexpectedErrorInReadingStepInfo"), e);
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

      rep.saveStepAttribute(idTransformation, idStep, "consumerKey",
          this.consumerKey);
      rep.saveStepAttribute(idTransformation, idStep, "consumerSecret",
          consumerSecret);
      rep.saveStepAttribute(idTransformation, idStep, "token",
          token);
      rep.saveStepAttribute(idTransformation, idStep, "secret",
          secret);
      rep.saveStepAttribute(idTransformation, idStep, "messageField",
          messageField);

    } catch (Exception e) {
      throw new KettleException(BaseMessages.getString(PKG,
          "TwitterStreamInput.Exception.UnableToSaveStepInfoToRepository") + idStep, e);
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
    return new PostTweetDialog(shell, (BaseStepMeta) meta, transMeta, name);
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
    return new PostTweet(stepMeta, stepDataInterface, cnr, transMeta, disp);
  }

  /**
   * Get a new instance of the appropriate data class. This data class implements the
   * StepDataInterface. It basically contains the persisting data that needs to live on, even if a
   * worker thread is terminated.
   * 
   * @return The appropriate StepDataInterface class.
   */
  public StepDataInterface getStepData() {
    return new PostTweetData();
  }

}
