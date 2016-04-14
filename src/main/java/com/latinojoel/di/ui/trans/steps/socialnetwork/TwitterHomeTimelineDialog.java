package com.latinojoel.di.ui.trans.steps.socialnetwork;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.pentaho.di.core.Const;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import com.latinojoel.di.trans.steps.socialnetwork.twitterhometimeline.TwitterHomeTimelineMeta;

/**
 * This class is responsible for the UI in Spoon of CouchBase memchached output step.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class TwitterHomeTimelineDialog extends BaseStepDialog implements StepDialogInterface {
  private static final Class<?> PKG = TwitterHomeTimelineDialog.class;

  private TwitterHomeTimelineMeta input;

  private TextVar wFieldOutput;
  private TextVar wConsumerKey;
  private TextVar wConsumerSecret;
  private TextVar wToken;
  private TextVar wSecret;

  public TwitterHomeTimelineDialog(Shell parent, Object in, TransMeta tr, String sname) {
    super(parent, (BaseStepMeta) in, tr, sname);
    input = (TwitterHomeTimelineMeta) in;
  }

  public String open() {
    final Shell parent = getParent();
    final Display display = parent.getDisplay();

    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
    props.setLook(shell);
    setShellImage(shell, input);

    final ModifyListener lsMod = new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        input.setChanged();
      }
    };
    changed = input.hasChanged();

    final FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = Const.FORM_MARGIN;
    formLayout.marginHeight = Const.FORM_MARGIN;

    shell.setLayout(formLayout);
    shell.setText(BaseMessages.getString(PKG, "TwitterHomeTimeline.Shell.Title"));

    final int middle = props.getMiddlePct();
    final int margin = Const.MARGIN;

    // Stepname line
    wlStepname = new Label(shell, SWT.RIGHT);
    wlStepname.setText(BaseMessages.getString(PKG, "TwitterHomeTimeline.Stepname.Label"));
    props.setLook(wlStepname);
    fdlStepname = new FormData();
    fdlStepname.left = new FormAttachment(0, 0);
    fdlStepname.right = new FormAttachment(middle, -margin);
    fdlStepname.top = new FormAttachment(0, margin);
    wlStepname.setLayoutData(fdlStepname);
    wStepname = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wStepname.setText(stepname);
    props.setLook(wStepname);
    wStepname.addModifyListener(lsMod);
    fdStepname = new FormData();
    fdStepname.left = new FormAttachment(middle, 0);
    fdStepname.top = new FormAttachment(0, margin);
    fdStepname.right = new FormAttachment(100, 0);
    wStepname.setLayoutData(fdStepname);

    // field output
    final Label wlFieldOutput = new Label(shell, SWT.RIGHT);
    wlFieldOutput.setText(BaseMessages.getString(PKG,
        "TwitterHomeTimeline.FieldOutput.Label"));
    wlFieldOutput.setToolTipText(BaseMessages.getString(PKG,
        "TwitterHomeTimeline.FieldOutput.Tooltip"));
    props.setLook(wlFieldOutput);
    final FormData fdlFieldOutput = new FormData();
    fdlFieldOutput.left = new FormAttachment(0, 0);
    fdlFieldOutput.right = new FormAttachment(middle, -margin);
    fdlFieldOutput.top = new FormAttachment(wStepname, margin);
    wlFieldOutput.setLayoutData(fdlFieldOutput);
    wFieldOutput = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wFieldOutput);
    wFieldOutput.addModifyListener(lsMod);
    final FormData fdFieldOutput = new FormData();
    fdFieldOutput.left = new FormAttachment(middle, 0);
    fdFieldOutput.top = new FormAttachment(wStepname, margin);
    fdFieldOutput.right = new FormAttachment(100, 0);
    wFieldOutput.setLayoutData(fdFieldOutput);

    // Consume key field
    final Label wlConsumerKey = new Label(shell, SWT.RIGHT);
    wlConsumerKey.setText(BaseMessages.getString(PKG, "TwitterHomeTimeline.ConsumerKey.Label"));
    props.setLook(wlConsumerKey);
    final FormData fdlConsumerKey = new FormData();
    fdlConsumerKey.left = new FormAttachment(0, 0);
    fdlConsumerKey.right = new FormAttachment(middle, -margin);
    fdlConsumerKey.top = new FormAttachment(wFieldOutput, margin);
    wlConsumerKey.setLayoutData(fdlConsumerKey);
    wConsumerKey = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wConsumerKey);
    wConsumerKey.addModifyListener(lsMod);
    final FormData fdConsumerKey = new FormData();
    fdConsumerKey.left = new FormAttachment(middle, 0);
    fdConsumerKey.top = new FormAttachment(wFieldOutput, margin);
    fdConsumerKey.right = new FormAttachment(100, 0);
    wConsumerKey.setLayoutData(fdConsumerKey);

    // Consume secret field
    final Label wlConsumerSecret = new Label(shell, SWT.RIGHT);
    wlConsumerSecret.setText(BaseMessages.getString(PKG,
        "TwitterHomeTimeline.ConsumerSecret.Label"));
    props.setLook(wlConsumerSecret);
    final FormData fdlConsumerSecret = new FormData();
    fdlConsumerSecret.left = new FormAttachment(0, 0);
    fdlConsumerSecret.right = new FormAttachment(middle, -margin);
    fdlConsumerSecret.top = new FormAttachment(wConsumerKey, margin);
    wlConsumerSecret.setLayoutData(fdlConsumerSecret);
    wConsumerSecret = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wConsumerSecret);
    wConsumerSecret.addModifyListener(lsMod);
    final FormData fdConsumerSecret = new FormData();
    fdConsumerSecret.left = new FormAttachment(middle, 0);
    fdConsumerSecret.top = new FormAttachment(wConsumerKey, margin);
    fdConsumerSecret.right = new FormAttachment(100, 0);
    wConsumerSecret.setLayoutData(fdConsumerSecret);

    // Token field
    final Label wlToken = new Label(shell, SWT.RIGHT);
    wlToken.setText(BaseMessages.getString(PKG, "TwitterHomeTimeline.Token.Label"));
    props.setLook(wlToken);
    final FormData fdlToken = new FormData();
    fdlToken.left = new FormAttachment(0, 0);
    fdlToken.right = new FormAttachment(middle, -margin);
    fdlToken.top = new FormAttachment(wConsumerSecret, margin);
    wlToken.setLayoutData(fdlToken);
    wToken = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wToken);
    wToken.addModifyListener(lsMod);
    final FormData fdToken = new FormData();
    fdToken.left = new FormAttachment(middle, 0);
    fdToken.top = new FormAttachment(wConsumerSecret, margin);
    fdToken.right = new FormAttachment(100, 0);
    wToken.setLayoutData(fdToken);

    // Secret field
    final Label wlSecret = new Label(shell, SWT.RIGHT);
    wlSecret.setText(BaseMessages.getString(PKG, "TwitterHomeTimeline.Secret.Label"));
    props.setLook(wlSecret);
    final FormData fdlSecret = new FormData();
    fdlSecret.left = new FormAttachment(0, 0);
    fdlSecret.right = new FormAttachment(middle, -margin);
    fdlSecret.top = new FormAttachment(wToken, margin);
    wlSecret.setLayoutData(fdlSecret);
    wSecret = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wSecret);
    wSecret.addModifyListener(lsMod);
    final FormData fdSecret = new FormData();
    fdSecret.left = new FormAttachment(middle, 0);
    fdSecret.top = new FormAttachment(wToken, margin);
    fdSecret.right = new FormAttachment(100, 0);
    wSecret.setLayoutData(fdSecret);

    // Some buttons
    wOK = new Button(shell, SWT.PUSH);
    wOK.setText(BaseMessages.getString(PKG, "System.Button.OK"));
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

    setButtonPositions(new Button[] {wOK, wCancel}, margin, null);

    // Add listeners
    lsCancel = new Listener() {
      public void handleEvent(Event e) {
        cancel();
      }
    };
    lsOK = new Listener() {
      public void handleEvent(Event e) {
        ok();
      }
    };

    wCancel.addListener(SWT.Selection, lsCancel);
    wOK.addListener(SWT.Selection, lsOK);

    lsDef = new SelectionAdapter() {
      public void widgetDefaultSelected(SelectionEvent e) {
        ok();
      }
    };

    wStepname.addSelectionListener(lsDef);

    // Detect X or ALT-F4 or something that kills this window...
    shell.addShellListener(new ShellAdapter() {
      public void shellClosed(ShellEvent e) {
        cancel();
      }
    });

    // Set the shell size, based upon previous time...
    setSize();

    getData();
    input.setChanged(changed);

    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch()) {
        display.sleep();
      }
    }
    return stepname;
  }

  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {
    if (!Const.isEmpty(input.getFieldOutput())) {
      wFieldOutput.setText(input.getFieldOutput());
    }
    wConsumerKey.setText(Const.NVL(input.getConsumerKey(), ""));
    wConsumerSecret.setText(Const.NVL(input.getConsumerSecret(), ""));
    wSecret.setText(Const.NVL(input.getSecret(), ""));
    wToken.setText(Const.NVL(input.getToken(), ""));

    wStepname.selectAll();
    wStepname.setFocus();
  }

  private void cancel() {
    stepname = null;
    input.setChanged(changed);
    dispose();
  }

  private void ok() {
    if (Const.isEmpty(wStepname.getText())) {
      return;
    }

    stepname = wStepname.getText(); // return value

    if (Const.isEmpty(wFieldOutput.getText())) {
      final MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
      mb.setMessage(BaseMessages.getString(PKG,
          "TwitterStreamInput.FieldOutput.Mandatory.DialogMessage"));
      mb.setText(BaseMessages.getString(PKG, "System.Dialog.Error.Title"));
      mb.open();
      return;
    }
    input.setFieldOutput(wFieldOutput.getText());

    if (Const.isEmpty(wConsumerKey.getText())) {
      final MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
      mb.setMessage(BaseMessages.getString(PKG,
          "TwitterStreamInput.ConsumerKey.Mandatory.DialogMessage"));
      mb.setText(BaseMessages.getString(PKG, "System.Dialog.Error.Title"));
      mb.open();
      return;
    }
    input.setConsumerKey(wConsumerKey.getText());
    if (Const.isEmpty(wConsumerSecret.getText())) {
      final MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
      mb.setMessage(BaseMessages.getString(PKG,
          "TwitterStreamInput.ConsumerSecret.Mandatory.DialogMessage"));
      mb.setText(BaseMessages.getString(PKG, "System.Dialog.Error.Title"));
      mb.open();
      return;
    }
    input.setConsumerSecret(wConsumerSecret.getText());
    input.setSecret(Const.NVL(wSecret.getText(), ""));
    input.setToken(Const.NVL(wToken.getText(), ""));

    dispose();
  }

}
