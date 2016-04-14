package com.latinojoel.di.ui.trans.steps.socialnetwork;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.ShellAdapter;
import org.eclipse.swt.events.ShellEvent;
import org.eclipse.swt.graphics.Cursor;
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
import org.pentaho.di.core.exception.KettleException;
import org.pentaho.di.core.row.RowMetaInterface;
import org.pentaho.di.i18n.BaseMessages;
import org.pentaho.di.trans.TransMeta;
import org.pentaho.di.trans.step.BaseStepMeta;
import org.pentaho.di.trans.step.StepDialogInterface;
import org.pentaho.di.ui.core.widget.TextVar;
import org.pentaho.di.ui.trans.step.BaseStepDialog;

import com.latinojoel.di.trans.steps.socialnetwork.fb.postmsg.PostMsgMeta;

/**
 * This class is responsible for the UI in Spoon of CouchBase memchached output step.
 * 
 * @author <a href="mailto:joel.latino@ivy-is.co.uk">Joel Latino</a>
 * @since 1.0.0
 */
public class PostMsgDialog extends BaseStepDialog implements StepDialogInterface {
  private static final Class<?> PKG = PostMsgDialog.class;

  private PostMsgMeta input;
  private boolean gotPreviousFields = false;
  private RowMetaInterface previousFields;

  private CCombo wMessageField;
  private TextVar wAppId;
  private TextVar wConsumerSecret;
  private TextVar wToken;
  private TextVar wPermissions;

  public PostMsgDialog(Shell parent, Object in, TransMeta tr, String sname) {
    super(parent, (BaseStepMeta) in, tr, sname);
    input = (PostMsgMeta) in;
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
    shell.setText(BaseMessages.getString(PKG, "FacebookNewsFeeds.Shell.Title"));

    final int middle = props.getMiddlePct();
    final int margin = Const.MARGIN;

    // Stepname line
    wlStepname = new Label(shell, SWT.RIGHT);
    wlStepname.setText(BaseMessages.getString(PKG, "FacebookNewsFeeds.Stepname.Label"));
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

    // message field
    final Label wlMessageField = new Label(shell, SWT.RIGHT);
    wlMessageField.setText(BaseMessages.getString(PKG,
        "FacebookNewsFeeds.MessageField.Label"));
    wlMessageField.setToolTipText(BaseMessages.getString(PKG,
        "FacebookNewsFeeds.MessageField.Tooltip"));
    props.setLook(wlMessageField);
    final FormData fdlMessageField = new FormData();
    fdlMessageField.left = new FormAttachment(0, 0);
    fdlMessageField.right = new FormAttachment(middle, -margin);
    fdlMessageField.top = new FormAttachment(wStepname, margin);
    wlMessageField.setLayoutData(fdlMessageField);
    wMessageField = new CCombo(shell, SWT.BORDER);
    props.setLook(wMessageField);
    wMessageField.addModifyListener(lsMod);
    final FormData fdMessageField = new FormData();
    fdMessageField.left = new FormAttachment(middle, 0);
    fdMessageField.top = new FormAttachment(wStepname, margin);
    fdMessageField.right = new FormAttachment(100, 0);
    wMessageField.setLayoutData(fdMessageField);
    wMessageField.addFocusListener(new FocusListener() {
      public void focusLost(org.eclipse.swt.events.FocusEvent e) {}

      public void focusGained(org.eclipse.swt.events.FocusEvent e) {
        final Cursor busy = new Cursor(shell.getDisplay(), SWT.CURSOR_WAIT);
        shell.setCursor(busy);
        getFieldsInto(wMessageField);
        shell.setCursor(null);
        busy.dispose();
      }
    });

    // AppId field
    final Label wlAppId = new Label(shell, SWT.RIGHT);
    wlAppId.setText(BaseMessages.getString(PKG, "FacebookNewsFeeds.AppId.Label"));
    props.setLook(wlAppId);
    final FormData fdlAppId = new FormData();
    fdlAppId.left = new FormAttachment(0, 0);
    fdlAppId.right = new FormAttachment(middle, -margin);
    fdlAppId.top = new FormAttachment(wMessageField, margin);
    wlAppId.setLayoutData(fdlAppId);
    wAppId = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wAppId);
    wAppId.addModifyListener(lsMod);
    final FormData fdAppId = new FormData();
    fdAppId.left = new FormAttachment(middle, 0);
    fdAppId.top = new FormAttachment(wMessageField, margin);
    fdAppId.right = new FormAttachment(100, 0);
    wAppId.setLayoutData(fdAppId);

    // Consume secret field
    final Label wlConsumerSecret = new Label(shell, SWT.RIGHT);
    wlConsumerSecret.setText(BaseMessages.getString(PKG,
        "FacebookNewsFeeds.ConsumerSecret.Label"));
    props.setLook(wlConsumerSecret);
    final FormData fdlConsumerSecret = new FormData();
    fdlConsumerSecret.left = new FormAttachment(0, 0);
    fdlConsumerSecret.right = new FormAttachment(middle, -margin);
    fdlConsumerSecret.top = new FormAttachment(wAppId, margin);
    wlConsumerSecret.setLayoutData(fdlConsumerSecret);
    wConsumerSecret = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wConsumerSecret);
    wConsumerSecret.addModifyListener(lsMod);
    final FormData fdConsumerSecret = new FormData();
    fdConsumerSecret.left = new FormAttachment(middle, 0);
    fdConsumerSecret.top = new FormAttachment(wAppId, margin);
    fdConsumerSecret.right = new FormAttachment(100, 0);
    wConsumerSecret.setLayoutData(fdConsumerSecret);

    // Token field
    final Label wlToken = new Label(shell, SWT.RIGHT);
    wlToken.setText(BaseMessages.getString(PKG, "FacebookNewsFeeds.Token.Label"));
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

    // Permissions field
    final Label wlPermissions = new Label(shell, SWT.RIGHT);
    wlPermissions.setText(BaseMessages.getString(PKG, "FacebookNewsFeeds.Permissions.Label"));
    props.setLook(wlPermissions);
    final FormData fdlPermissions = new FormData();
    fdlPermissions.left = new FormAttachment(0, 0);
    fdlPermissions.right = new FormAttachment(middle, -margin);
    fdlPermissions.top = new FormAttachment(wToken, margin);
    wlPermissions.setLayoutData(fdlPermissions);
    wPermissions = new TextVar(transMeta, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    props.setLook(wPermissions);
    wPermissions.addModifyListener(lsMod);
    final FormData fdPermissions = new FormData();
    fdPermissions.left = new FormAttachment(middle, 0);
    fdPermissions.top = new FormAttachment(wToken, margin);
    fdPermissions.right = new FormAttachment(100, 0);
    wPermissions.setLayoutData(fdPermissions);

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


  private void getFieldsInto(CCombo fieldCombo) {
    try {
      if (!gotPreviousFields) {
        previousFields = transMeta.getPrevStepFields(stepname);
      }

      final String field = fieldCombo.getText();

      if (previousFields != null) {
        fieldCombo.setItems(previousFields.getFieldNames());
      }

      if (field != null) {
        fieldCombo.setText(field);
      }
      gotPreviousFields = true;

    } catch (KettleException ke) {
      return;
    }
  }

  /**
   * Copy information from the meta-data input to the dialog fields.
   */
  public void getData() {
    if (!Const.isEmpty(input.getMessageField())) {
      wMessageField.setText(input.getMessageField());
    }
    wAppId.setText(Const.NVL(input.getAppId(), ""));
    wConsumerSecret.setText(Const.NVL(input.getAppSecret(), ""));
    wPermissions.setText(Const.NVL(input.getPermissions(), ""));
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

    if (Const.isEmpty(wMessageField.getText())) {
      final MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
      mb.setMessage(BaseMessages.getString(PKG,
          "PostTweet.MessageField.DialogMessage"));
      mb.setText(BaseMessages.getString(PKG, "System.Dialog.Error.Title"));
      mb.open();
      return;
    }
    input.setMessageField(wMessageField.getText());

    if (Const.isEmpty(wAppId.getText())) {
      final MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
      mb.setMessage(BaseMessages.getString(PKG,
          "PostTweet.ComsumerKey.Mandatory.DialogMessage"));
      mb.setText(BaseMessages.getString(PKG, "System.Dialog.Error.Title"));
      mb.open();
      return;
    }
    input.setAppId(wAppId.getText());
    if (Const.isEmpty(wConsumerSecret.getText())) {
      final MessageBox mb = new MessageBox(shell, SWT.OK | SWT.ICON_ERROR);
      mb.setMessage(BaseMessages.getString(PKG,
          "PostTweet.ConsumerSecret.DialogMessage"));
      mb.setText(BaseMessages.getString(PKG, "System.Dialog.Error.Title"));
      mb.open();
      return;
    }
    input.setAppSecret(wConsumerSecret.getText());
    input.setPermissions(Const.NVL(wPermissions.getText(), ""));
    input.setToken(Const.NVL(wToken.getText(), ""));

    dispose();

  }

}
