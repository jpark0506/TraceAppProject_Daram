/*
 * Copyright (c) 2018, Nordic Semiconductor
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *
 * 3. Neither the name of the copyright holder nor the names of its contributors may be used to endorse or promote products derived from this
 * software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
 * LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
 * HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE
 * USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package no.nordicsemi.android.blinky.profile.callback;

import android.bluetooth.BluetoothDevice;

import androidx.annotation.NonNull;

import com.example.traceappproject_daram.data.Cons;
import com.example.traceappproject_daram.data.Result;

import no.nordicsemi.android.ble.callback.profile.ProfileDataCallback;
import no.nordicsemi.android.ble.data.Data;
import no.nordicsemi.android.blinky.profile.data.Constants;

@SuppressWarnings("ConstantConditions")

// 지금은 안쓰는 클래스
//blinkyButtonDataCallback을 2개를 생성하기 때문에 각각 left만 right만 받을 것이다.
public abstract class BlinkyButtonDataCallbackLRSep implements ProfileDataCallback, BlinkyButtonCallback {
    private static final String TAG= "MJBUTTONCALLBACK";
    private byte EMPTY_PASTMODE = 0x11;//-1
    private byte pastMode;
    private int idx=0;
    private byte[] bytes;
    private byte[] leftBytes;
    private byte[] rightBytes;
    private Result result;
    private long beforeTime;
    private int inv =0;
    //version 정보도 따로 파
    public BlinkyButtonDataCallbackLRSep(Result result){
        this.result = result;
    }
    @Override
    public void onDataReceived(@NonNull final BluetoothDevice device, @NonNull final Data data) {
        if (data.size() != 1) {
            onInvalidDataReceived(device, data);
            return;
        }
        //Log.i(TAG,"data recieved"+idx +" : "+data.toString());
        if(idx ==1){
            beforeTime = System.currentTimeMillis();
        }
        byte oneb = data.getByte(0);
        if(oneb==pastMode+1||(pastMode ==0xff&&oneb==0x00)){

        }
        else{
            inv++;
            //Log.i(TAG,"패킷로스 "+idx +" : "+inv);
        }
        idx++;
        pastMode =oneb;
        /*
        if(idx%12 ==0){
            Log.i(TAG,"========================================");
        }
        if(idx>70) {
            long afterTime = System.currentTimeMillis(); // 코드 실행 후에 시간 받아오기
            long msecDiffTime = (afterTime - beforeTime); //두 시간에 차 계산
            System.out.println("시간차이(m) : " + msecDiffTime);
        }

         */

        /*
        final int state = data.getIntValue(Data.FORMAT_UINT8, 0);
        if (state == STATE_PRESSED) {
            onButtonStateChanged(device, true);
        } else if (state == STATE_RELEASED) {
            onButtonStateChanged(device, false);
        } else {
            onInvalidDataReceived(device, data);
        }
        */

        //byte oneb = data.getByte(0);
        //나중에 파싱 일단 걍 바이트 어레이에 넣어!
        if(pastMode==EMPTY_PASTMODE){
            pastMode = oneb;
            idx=0;
            if(pastMode == Constants.MODE_MEASURE_LEFT||pastMode == Constants.MODE_MEASURE_RIGHT){
                //시간 되면 left->right인지도 검사하기
                bytes = new byte[Cons.SENSOR_NUM_FOOT];

            }
            if(pastMode == Constants.MODE_VERSION){
                //do nothing
            }
            if(pastMode == Constants.MODE_MEASURE_END){
                //block 돼 있던 거 풀어야댐
                onMeasureEnd(device,data);
            }
        }
        else{ //EMPTY가 아님
            if(pastMode == Constants.MODE_RUN){
                idx = 0;
            }
            else if(pastMode == Constants.MODE_MEASURE_LEFT){
                bytes[idx] = oneb;
                idx++;
                if(idx == Cons.SENSOR_NUM_FOOT){
                    pastMode = EMPTY_PASTMODE;
                    //append to result
                    //result.appendOneFrame(bytes);//왼발 오른발 구분은 left->right라 상관 ㄴ
                }
            }
            else if(pastMode == Constants.MODE_MEASURE_RIGHT){
                bytes[idx] = oneb;
                idx++;
                if(idx == Cons.SENSOR_NUM_FOOT){
                    pastMode = EMPTY_PASTMODE;
                    //append to result
                    //result.appendOneFrame(bytes);//왼발 오른발 구분은 left->right라 상관 ㄴ
                }
            }
            if(pastMode == Constants.MODE_VERSION){
                result.setVersion(3);
            }
        }

    }
}