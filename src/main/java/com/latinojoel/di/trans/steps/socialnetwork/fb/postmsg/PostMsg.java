package com.latinojoel.di.trans.steps.socialnetwork.fb.postmsg;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import facebook4j.FacebookFactory;
import facebook4j.conf.ConfigurationBuilder;

/**
 * This class is responsible to processing the data rows.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class PostMsg extends BaseStep implements StepInterface {

  private static final Class<?> PKG = PostMsg.class;
  private PostMsgMeta meta;
  private PostMsgData data;

  public PostMsg(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t,
      Trans dis) {
    super(s, stepDataInterface, c, t, dis);
  }

  /**
   * {@inheritDoc}
   * 
   * @throws KettleException
   */
  @Override
  public boolean processRow(StepMetaInterface smi, StepDataInterface sdi) throws KettleException {
    meta = (PostMsgMeta) smi;
    data = (PostMsgData) sdi;

    final Object[] r = getRow();

    if (r == null) {
      setOutputDone();
      return false;
    }

    try {
      if (first) {
        first = false;
        data.outputRowMeta = getInputRowMeta().clone();
        meta.getFields(data.outputRowMeta, getStepname(), null, null, this, repository, metaStore);
      }

      final int messageFieldIndex = getInputRowMeta().indexOfValue(
          meta.getMessageField());
      if (messageFieldIndex < 0) {
        throw new KettleException(BaseMessages.getString(PKG,
            "FacebookNewsFeeds.Exception.MessageFieldNotFound"));
      }
      data.facebook.postStatusMessage((String) (r[messageFieldIndex]));

    } catch (Exception ex) {
      logError("Exception: " + ex.getMessage(), ex);
      throw new KettleException(BaseMessages.getString(PKG,
          "FacebookNewsFeeds.InterruptedException.Message"));
    }

    if (checkFeedback(getLinesRead())) {
      if (log.isBasic()) {
        logBasic(BaseMessages.getString(PKG, "FacebookNewsFeeds.Log.LineNumber") + getLinesRead());
      }
    }

    return true;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
    meta = (PostMsgMeta) smi;
    data = (PostMsgData) sdi;

    if (super.init(smi, sdi)) {

      final ConfigurationBuilder cb = new ConfigurationBuilder();
      cb.setOAuthAppId(environmentSubstitute(meta.getAppId()))
          .setOAuthAppSecret(environmentSubstitute(meta.getAppSecret()))
          .setOAuthAccessToken(environmentSubstitute(meta.getToken()))
          .setOAuthPermissions(environmentSubstitute(meta.getPermissions()));
      final FacebookFactory ff = new FacebookFactory(cb.build());
      data.facebook = ff.getInstance();

      return true;
    }
    return false;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void dispose(StepMetaInterface smi, StepDataInterface sdi) {
    super.dispose(smi, sdi);
  }

  /**
   * Run is were the action happens.
   */
  public void run() {
    logBasic("Starting to run...");
    try {
      while (processRow(meta, data) && !isStopped()) {
        continue;
      }
    } catch (Exception e) {
      // logError("Unexpected error : " + e.toString());
      logError(Const.getStackTracker(e));
      setErrors(1);
      stopAll();
    } finally {
      dispose(meta, data);
      logBasic("Finished, processing " + getLinesRead() + " rows");
      markStop();
    }
  }
}
