package com.softwaregiants.careertinder.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.softwaregiants.careertinder.R;
import com.softwaregiants.careertinder.models.ChatModel;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends BaseActivity {
	private DatabaseReference mFirebaseDatabaseReference;
	public static String applicant;
	public static final String TAG = ChatActivity.class.getSimpleName();
	public static String company;
	public static String currentUser;
	public static final String MESSAGES_CHILD = "messages";
	private ImageButton mSendButton;
	private EditText ETMessage;
	private RecyclerView mMessageRecyclerView;
	private LinearLayoutManager mLinearLayoutManager;
	AlertDialog alertDialog;

	private FirebaseRecyclerAdapter<ChatModel, MessageViewHolder> mAdapter;

	@Override
	public void onCreate( @Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat);
		currentUser = getIntent().getStringExtra("currentUser");
		applicant = getIntent().getStringExtra("applicant");
		company = getIntent().getStringExtra("company");
		Log.d(TAG, "onCreate: current User:" + currentUser);
		init();
	}

	@Override
	public void onStart() {
		super.onStart();
		// Check if user is signed in.
	}

	@Override
	public void onPause() {
		if (mAdapter != null)
			mAdapter.stopListening();
		super.onPause();
	}

	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.startListening();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

	private void init() {
		createProgress(this);
		mSendButton = findViewById(R.id.btnSubmit);
		mSendButton.setOnClickListener(onClickListener);
		ETMessage = findViewById(R.id.messageEditText);
		mMessageRecyclerView = findViewById(R.id.messageRecyclerView);
		mLinearLayoutManager = new LinearLayoutManager(this);
		mLinearLayoutManager.setStackFromEnd(true);
		mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
		loadMessages();
	}

	private void loadMessages() {
		mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference(applicant + "_" + company);

		Query query = mFirebaseDatabaseReference.child(MESSAGES_CHILD);
		query.addListenerForSingleValueEvent(new ValueEventListener() {
			@Override
			public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
				if (! dataSnapshot.exists() ) {
					cancelProgress();
				}
			}

			@Override
			public void onCancelled(@NonNull DatabaseError databaseError) {
				cancelProgress();
				Toast.makeText(mContext,"There was an error loading your messages. Please try again.",Toast.LENGTH_SHORT).show();
				finish();
			}
		});

		SnapshotParser<ChatModel> parser = new SnapshotParser<ChatModel>() {
			@NonNull
			@Override
			public ChatModel parseSnapshot(DataSnapshot dataSnapshot) {
				ChatModel chatModel = dataSnapshot.getValue(ChatModel.class);
				if (chatModel != null) {
					chatModel.setId(dataSnapshot.getKey());
				}
				return chatModel;
			}
		};

		FirebaseRecyclerOptions<ChatModel> options =
				new FirebaseRecyclerOptions.Builder<ChatModel>()
						.setQuery(query, parser)
						.build();

		mAdapter = new FirebaseRecyclerAdapter<ChatModel, MessageViewHolder>(options) {

			static final int VT_SENT = 97567;
			static final int VT_RECD = 97568;

			@Override
			protected void onBindViewHolder(@NonNull MessageViewHolder holder, int position, @NonNull ChatModel model) {
				cancelProgress();
				holder.messageTextView.setText(model.getMessage());
			}

			@NonNull
			@Override
			public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
				LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
				MessageViewHolder messageViewHolder;
				if ( i == VT_SENT ) {
					messageViewHolder = new MessageViewHolder(inflater.inflate(R.layout.item_msg_sent, viewGroup, false));
				} else {
					messageViewHolder = new MessageViewHolder(inflater.inflate(R.layout.item_msg_recd, viewGroup, false));
				}
				return messageViewHolder;
			}

			@Override
			public int getItemViewType(int position) {
				if (getItem(position).getSender().equals(currentUser)) {
					return VT_SENT;
				} else {
					return VT_RECD;
				}
			}
		};



		mAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
			@Override
			public void onItemRangeInserted(int positionStart, int itemCount) {
				super.onItemRangeInserted(positionStart, itemCount);
				int friendlyMessageCount = mAdapter.getItemCount();
				int lastVisiblePosition =
						mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
				// If the recycler view is initially being loaded or the
				// user is at the bottom of the list, scroll to the bottom
				// of the list to show the newly added message.
				if (lastVisiblePosition == -1 ||
						(positionStart >= (friendlyMessageCount - 1) &&
								lastVisiblePosition == (positionStart - 1))) {
					mMessageRecyclerView.scrollToPosition(positionStart);
				}
			}
		});

		mMessageRecyclerView.setAdapter(mAdapter);
		mAdapter.startListening();
	}

	public static class MessageViewHolder extends RecyclerView.ViewHolder {
		TextView messageTextView;
		ImageView messageImageView;
		TextView messengerTextView;
		CircleImageView messengerImageView;

		MessageViewHolder(View v) {
			super(v);
			messageTextView = itemView.findViewById(R.id.messageTextView);
			messageImageView = itemView.findViewById(R.id.messageImageView);
			messengerTextView = itemView.findViewById(R.id.messengerTextView);
			messengerImageView = itemView.findViewById(R.id.messengerImageView);
		}
	}

	View.OnClickListener onClickListener = new View.OnClickListener() {
		@Override
		public void onClick(View v) {
				if ( ETMessage.getText()!=null && !ETMessage.getText().toString().trim().isEmpty() ) {
					ChatModel friendlyMessage = new ChatModel(currentUser, ETMessage.getText().toString().trim());
					mFirebaseDatabaseReference.child(MESSAGES_CHILD).push().setValue(friendlyMessage);
					ETMessage.setText("");
				}
		}
	};

	public void createProgress(Context activityContext) {
		final View view = ((LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE))
				.inflate(R.layout.alert_progress, null);
		alertDialog = new AlertDialog.Builder(activityContext).create();
		alertDialog.setCancelable(false);
		alertDialog.setView(view);
		alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
		alertDialog.show();
	}

	public void cancelProgress() {
		if (alertDialog!=null) {
			alertDialog.dismiss();
			alertDialog = null;
		}
	}


}