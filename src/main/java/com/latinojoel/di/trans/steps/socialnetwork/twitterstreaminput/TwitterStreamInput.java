package com.latinojoel.di.trans.steps.socialnetwork.twitterstreaminput;

import java.util.concurrent.LinkedBlockingQueue;

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
import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.conf.ConfigurationBuilder;

import com.google.common.collect.Lists;
import com.twitter.hbc.ClientBuilder;
import com.twitter.hbc.core.Constants;
import com.twitter.hbc.core.endpoint.StatusesFilterEndpoint;
import com.twitter.hbc.core.processor.StringDelimitedProcessor;
import com.twitter.hbc.httpclient.auth.Authentication;
import com.twitter.hbc.httpclient.auth.OAuth1;
import com.twitter.hbc.twitter4j.Twitter4jStatusClient;

/**
 * This class is responsible to processing the data rows.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class TwitterStreamInput extends BaseStep implements StepInterface {

  private TwitterStreamInputMeta meta;
  private TwitterStreamInputData data;

  public TwitterStreamInput(StepMeta s, StepDataInterface stepDataInterface, int c, TransMeta t,
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

      for (int msgRead = 0; msgRead < data.queueSize; msgRead++) {
        Object row[] = null;

        row = RowDataUtil.allocateRowData(data.outputRowMeta.size());
        int index = 0;
        row[index++] = data.queue.take();

        putRow(data.outputRowMeta, row);
        data.rowNum++;
      }

      data.client.stop();
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
    meta = (TwitterStreamInputMeta) smi;
    data = (TwitterStreamInputData) sdi;

    if (super.init(smi, sdi)) {

      data.queueSize = Integer.parseInt(environmentSubstitute(meta
          .getQueueSize()));
      data.queue =
          new LinkedBlockingQueue<String>(data.queueSize);

      final StatusesFilterEndpoint endpoint = new StatusesFilterEndpoint();
      // add some track terms
      endpoint.trackTerms(Lists.newArrayList(meta.getTrackTerms().split(",")));

      final Authentication auth =
          new OAuth1(environmentSubstitute(meta.getConsumerKey()),
              environmentSubstitute(meta.getConsumerSecret()),
              environmentSubstitute(meta.getToken()), environmentSubstitute(meta.getSecret()));

      // Create a new BasicClient. By default gzip is enabled.
      data.client = new ClientBuilder()
          .hosts(Constants.STREAM_HOST)
          .endpoint(endpoint)
          .authentication(auth)
          .processor(new StringDelimitedProcessor(data.queue))
          .build();

      // Establish a connection
      data.client.connect();

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
