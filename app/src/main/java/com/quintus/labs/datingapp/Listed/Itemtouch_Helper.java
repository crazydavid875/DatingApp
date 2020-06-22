package com.quintus.labs.datingapp.Listed;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class Itemtouch_Helper extends ItemTouchHelper.Callback {

        private final ItemTouchHelperContract mAdapter;

        public Itemtouch_Helper(ItemTouchHelperContract adapter) {
            mAdapter = adapter;
        }



        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public boolean isItemViewSwipeEnabled() {
            return false;
        }



        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        }

        @Override
        public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
            int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
            return makeMovementFlags(dragFlags, 0);
        }

        @Override
        public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                              RecyclerView.ViewHolder target) {
            mAdapter.onRowMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
            return true;
        }

        @Override
        public void onSelectedChanged(RecyclerView.ViewHolder viewHolder,
                                      int actionState) {


            if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
                if (viewHolder instanceof McListAdapter.MyViewHolder) {
                        McListAdapter.MyViewHolder myViewHolder=
                            (McListAdapter.MyViewHolder) viewHolder;
                    mAdapter.onRowSelected(myViewHolder);
                }

            }

            super.onSelectedChanged(viewHolder, actionState);
        }
        @Override
        public void clearView(RecyclerView recyclerView,
                              RecyclerView.ViewHolder viewHolder) {
            super.clearView(recyclerView, viewHolder);

            if (viewHolder instanceof McListAdapter.MyViewHolder) {
                    McListAdapter.MyViewHolder myViewHolder=
                        (McListAdapter.MyViewHolder) viewHolder;
                mAdapter.onRowClear(myViewHolder);
            }
        }

        public interface ItemTouchHelperContract {

            void onRowMoved(int fromPosition, int toPosition);
            void onRowSelected(McListAdapter.MyViewHolder myViewHolder);
            void onRowClear(McListAdapter.MyViewHolder myViewHolder);

        }



}
