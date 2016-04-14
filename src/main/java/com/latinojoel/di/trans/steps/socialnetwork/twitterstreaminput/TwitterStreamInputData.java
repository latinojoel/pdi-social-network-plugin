package com.latinojoel.di.trans.steps.socialnetwork.twitterstreaminput;

import java.util.concurrent.BlockingQueue;

import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.trans.step.BaseStepData;
import org.pentaho.di.trans.step.StepDataInterface;

import com.twitter.hbc.core.Client;

/**
 * This class contains the methods to set and retrieve the status of the step data.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class TwitterStreamInputData extends BaseStepData implements StepDataInterface {

  RowMetaInterface outputRowMeta;
  int nrPrevFields;
  int rowNum = 0;
  RowMetaInterface convertRowMeta;
  BlockingQueue<String> queue;
  Client client;
  int queueSize;

}
