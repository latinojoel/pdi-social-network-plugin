package com.latinojoel.di.trans.steps.socialnetwork.twitterhometimeline;

import org.pentaho.di.core.Const;
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowDataUtil;
import org.pentaho.di.core.row.RowMeta;
import org.pentaho.di.trans.Trans;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStep;
import org.pentaho.di.trans.step.StepDataInterface;
import org.pentaho.di.trans.step.StepInterface;
import org.pentaho.di.trans.step.StepMeta;
import org.pentaho.di.trans.step.StepMetaInterface;

import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

/**
 * This class is responsible to processing the data rows.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class TwitterHomeTimeline extends BaseStep implements StepInterface {

  private TwitterHomeTimelineMeta meta;
  private TwitterHomeTimelineData data;

  public TwitterHomeTimeline(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t,
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
    try {

      if (first) {
        first = false;
        data.outputRowMeta = new RowMeta();
        data.nrPrevFields = data.outputRowMeta.size();
        meta.getFields(data.outputRowMeta, getStepname(), null, null, this);

      }

      for (Status status : data.statuses) {
        Object row[] = null;

        row = RowDataUtil.allocateRowData(data.outputRowMeta.size());
        int index = 0;
        row[index++] = status.getText();

        putRow(data.outputRowMeta, row);
        data.rowNum++;
      }

      setOutputDone();
      logDebug("Finished, processing all rows");
      return false;


    } catch (Exception e) {
      if (e instanceof KettleException) {
        throw (KettleException) e;
      } else {
        throw new KettleException(e);
      }
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public boolean init(StepMetaInterface smi, StepDataInterface sdi) {
    meta = (TwitterHomeTimelineMeta) smi;
    data = (TwitterHomeTimelineData) sdi;

    if (super.init(smi, sdi)) {

      final ConfigurationBuilder cb = new ConfigurationBuilder();
      cb.setOAuthConsumerKey(environmentSubstitute(meta.getConsumerKey()))
          .setOAuthConsumerSecret(environmentSubstitute(meta.getConsumerSecret()))
          .setOAuthAccessToken(environmentSubstitute(meta.getToken()))
          .setOAuthAccessTokenSecret(environmentSubstitute(meta.getSecret()));
      final TwitterFactory tf = new TwitterFactory(cb.build());
      data.twitter = tf.getInstance();
      try {
        data.statuses = data.twitter.getHomeTimeline();
      } catch (TwitterException e) {
        e.printStackTrace();
      }

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
