// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: home/paolo/uni/dissertation/dmat/proto/OrderAddAssignWire.proto

package it.unipr.aotlab.dmat.core.generated;

public final class OrderAddAssignWire {
  private OrderAddAssignWire() {}
  public static void registerAllExtensions(
      com.google.protobuf.ExtensionRegistry registry) {
  }
  public interface OrderAddAssignBodyOrBuilder
      extends com.google.protobuf.MessageOrBuilder {
    
    // required int32 presenceStatus = 1;
    boolean hasPresenceStatus();
    int getPresenceStatus();
    
    // required string firstAddendumAssign = 2;
    boolean hasFirstAddendumAssign();
    String getFirstAddendumAssign();
    
    // required string secondAddendum = 3;
    boolean hasSecondAddendum();
    String getSecondAddendum();
    
    // required .RectangleBody sumArea = 4;
    boolean hasSumArea();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getSumArea();
    it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getSumAreaOrBuilder();
  }
  public static final class OrderAddAssignBody extends
      com.google.protobuf.GeneratedMessage
      implements OrderAddAssignBodyOrBuilder {
    // Use OrderAddAssignBody.newBuilder() to construct.
    private OrderAddAssignBody(Builder builder) {
      super(builder);
    }
    private OrderAddAssignBody(boolean noInit) {}
    
    private static final OrderAddAssignBody defaultInstance;
    public static OrderAddAssignBody getDefaultInstance() {
      return defaultInstance;
    }
    
    public OrderAddAssignBody getDefaultInstanceForType() {
      return defaultInstance;
    }
    
    public static final com.google.protobuf.Descriptors.Descriptor
        getDescriptor() {
      return it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.internal_static_OrderAddAssignBody_descriptor;
    }
    
    protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
        internalGetFieldAccessorTable() {
      return it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.internal_static_OrderAddAssignBody_fieldAccessorTable;
    }
    
    private int bitField0_;
    // required int32 presenceStatus = 1;
    public static final int PRESENCESTATUS_FIELD_NUMBER = 1;
    private int presenceStatus_;
    public boolean hasPresenceStatus() {
      return ((bitField0_ & 0x00000001) == 0x00000001);
    }
    public int getPresenceStatus() {
      return presenceStatus_;
    }
    
    // required string firstAddendumAssign = 2;
    public static final int FIRSTADDENDUMASSIGN_FIELD_NUMBER = 2;
    private java.lang.Object firstAddendumAssign_;
    public boolean hasFirstAddendumAssign() {
      return ((bitField0_ & 0x00000002) == 0x00000002);
    }
    public String getFirstAddendumAssign() {
      java.lang.Object ref = firstAddendumAssign_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          firstAddendumAssign_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getFirstAddendumAssignBytes() {
      java.lang.Object ref = firstAddendumAssign_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        firstAddendumAssign_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required string secondAddendum = 3;
    public static final int SECONDADDENDUM_FIELD_NUMBER = 3;
    private java.lang.Object secondAddendum_;
    public boolean hasSecondAddendum() {
      return ((bitField0_ & 0x00000004) == 0x00000004);
    }
    public String getSecondAddendum() {
      java.lang.Object ref = secondAddendum_;
      if (ref instanceof String) {
        return (String) ref;
      } else {
        com.google.protobuf.ByteString bs = 
            (com.google.protobuf.ByteString) ref;
        String s = bs.toStringUtf8();
        if (com.google.protobuf.Internal.isValidUtf8(bs)) {
          secondAddendum_ = s;
        }
        return s;
      }
    }
    private com.google.protobuf.ByteString getSecondAddendumBytes() {
      java.lang.Object ref = secondAddendum_;
      if (ref instanceof String) {
        com.google.protobuf.ByteString b = 
            com.google.protobuf.ByteString.copyFromUtf8((String) ref);
        secondAddendum_ = b;
        return b;
      } else {
        return (com.google.protobuf.ByteString) ref;
      }
    }
    
    // required .RectangleBody sumArea = 4;
    public static final int SUMAREA_FIELD_NUMBER = 4;
    private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody sumArea_;
    public boolean hasSumArea() {
      return ((bitField0_ & 0x00000008) == 0x00000008);
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getSumArea() {
      return sumArea_;
    }
    public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getSumAreaOrBuilder() {
      return sumArea_;
    }
    
    private void initFields() {
      presenceStatus_ = 0;
      firstAddendumAssign_ = "";
      secondAddendum_ = "";
      sumArea_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
    }
    private byte memoizedIsInitialized = -1;
    public final boolean isInitialized() {
      byte isInitialized = memoizedIsInitialized;
      if (isInitialized != -1) return isInitialized == 1;
      
      if (!hasPresenceStatus()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasFirstAddendumAssign()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasSecondAddendum()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!hasSumArea()) {
        memoizedIsInitialized = 0;
        return false;
      }
      if (!getSumArea().isInitialized()) {
        memoizedIsInitialized = 0;
        return false;
      }
      memoizedIsInitialized = 1;
      return true;
    }
    
    public void writeTo(com.google.protobuf.CodedOutputStream output)
                        throws java.io.IOException {
      getSerializedSize();
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        output.writeInt32(1, presenceStatus_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        output.writeBytes(2, getFirstAddendumAssignBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        output.writeBytes(3, getSecondAddendumBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        output.writeMessage(4, sumArea_);
      }
      getUnknownFields().writeTo(output);
    }
    
    private int memoizedSerializedSize = -1;
    public int getSerializedSize() {
      int size = memoizedSerializedSize;
      if (size != -1) return size;
    
      size = 0;
      if (((bitField0_ & 0x00000001) == 0x00000001)) {
        size += com.google.protobuf.CodedOutputStream
          .computeInt32Size(1, presenceStatus_);
      }
      if (((bitField0_ & 0x00000002) == 0x00000002)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(2, getFirstAddendumAssignBytes());
      }
      if (((bitField0_ & 0x00000004) == 0x00000004)) {
        size += com.google.protobuf.CodedOutputStream
          .computeBytesSize(3, getSecondAddendumBytes());
      }
      if (((bitField0_ & 0x00000008) == 0x00000008)) {
        size += com.google.protobuf.CodedOutputStream
          .computeMessageSize(4, sumArea_);
      }
      size += getUnknownFields().getSerializedSize();
      memoizedSerializedSize = size;
      return size;
    }
    
    private static final long serialVersionUID = 0L;
    @java.lang.Override
    protected java.lang.Object writeReplace()
        throws java.io.ObjectStreamException {
      return super.writeReplace();
    }
    
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(
        com.google.protobuf.ByteString data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(
        com.google.protobuf.ByteString data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(byte[] data)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(
        byte[] data,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws com.google.protobuf.InvalidProtocolBufferException {
      return newBuilder().mergeFrom(data, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(java.io.InputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseDelimitedFrom(java.io.InputStream input)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseDelimitedFrom(
        java.io.InputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      Builder builder = newBuilder();
      if (builder.mergeDelimitedFrom(input, extensionRegistry)) {
        return builder.buildParsed();
      } else {
        return null;
      }
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(
        com.google.protobuf.CodedInputStream input)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input).buildParsed();
    }
    public static it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody parseFrom(
        com.google.protobuf.CodedInputStream input,
        com.google.protobuf.ExtensionRegistryLite extensionRegistry)
        throws java.io.IOException {
      return newBuilder().mergeFrom(input, extensionRegistry)
               .buildParsed();
    }
    
    public static Builder newBuilder() { return Builder.create(); }
    public Builder newBuilderForType() { return newBuilder(); }
    public static Builder newBuilder(it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody prototype) {
      return newBuilder().mergeFrom(prototype);
    }
    public Builder toBuilder() { return newBuilder(this); }
    
    @java.lang.Override
    protected Builder newBuilderForType(
        com.google.protobuf.GeneratedMessage.BuilderParent parent) {
      Builder builder = new Builder(parent);
      return builder;
    }
    public static final class Builder extends
        com.google.protobuf.GeneratedMessage.Builder<Builder>
       implements it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBodyOrBuilder {
      public static final com.google.protobuf.Descriptors.Descriptor
          getDescriptor() {
        return it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.internal_static_OrderAddAssignBody_descriptor;
      }
      
      protected com.google.protobuf.GeneratedMessage.FieldAccessorTable
          internalGetFieldAccessorTable() {
        return it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.internal_static_OrderAddAssignBody_fieldAccessorTable;
      }
      
      // Construct using it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody.newBuilder()
      private Builder() {
        maybeForceBuilderInitialization();
      }
      
      private Builder(BuilderParent parent) {
        super(parent);
        maybeForceBuilderInitialization();
      }
      private void maybeForceBuilderInitialization() {
        if (com.google.protobuf.GeneratedMessage.alwaysUseFieldBuilders) {
          getSumAreaFieldBuilder();
        }
      }
      private static Builder create() {
        return new Builder();
      }
      
      public Builder clear() {
        super.clear();
        presenceStatus_ = 0;
        bitField0_ = (bitField0_ & ~0x00000001);
        firstAddendumAssign_ = "";
        bitField0_ = (bitField0_ & ~0x00000002);
        secondAddendum_ = "";
        bitField0_ = (bitField0_ & ~0x00000004);
        if (sumAreaBuilder_ == null) {
          sumArea_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
        } else {
          sumAreaBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      
      public Builder clone() {
        return create().mergeFrom(buildPartial());
      }
      
      public com.google.protobuf.Descriptors.Descriptor
          getDescriptorForType() {
        return it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody.getDescriptor();
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody getDefaultInstanceForType() {
        return it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody.getDefaultInstance();
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody build() {
        it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(result);
        }
        return result;
      }
      
      private it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody buildParsed()
          throws com.google.protobuf.InvalidProtocolBufferException {
        it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody result = buildPartial();
        if (!result.isInitialized()) {
          throw newUninitializedMessageException(
            result).asInvalidProtocolBufferException();
        }
        return result;
      }
      
      public it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody buildPartial() {
        it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody result = new it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody(this);
        int from_bitField0_ = bitField0_;
        int to_bitField0_ = 0;
        if (((from_bitField0_ & 0x00000001) == 0x00000001)) {
          to_bitField0_ |= 0x00000001;
        }
        result.presenceStatus_ = presenceStatus_;
        if (((from_bitField0_ & 0x00000002) == 0x00000002)) {
          to_bitField0_ |= 0x00000002;
        }
        result.firstAddendumAssign_ = firstAddendumAssign_;
        if (((from_bitField0_ & 0x00000004) == 0x00000004)) {
          to_bitField0_ |= 0x00000004;
        }
        result.secondAddendum_ = secondAddendum_;
        if (((from_bitField0_ & 0x00000008) == 0x00000008)) {
          to_bitField0_ |= 0x00000008;
        }
        if (sumAreaBuilder_ == null) {
          result.sumArea_ = sumArea_;
        } else {
          result.sumArea_ = sumAreaBuilder_.build();
        }
        result.bitField0_ = to_bitField0_;
        onBuilt();
        return result;
      }
      
      public Builder mergeFrom(com.google.protobuf.Message other) {
        if (other instanceof it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody) {
          return mergeFrom((it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody)other);
        } else {
          super.mergeFrom(other);
          return this;
        }
      }
      
      public Builder mergeFrom(it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody other) {
        if (other == it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody.getDefaultInstance()) return this;
        if (other.hasPresenceStatus()) {
          setPresenceStatus(other.getPresenceStatus());
        }
        if (other.hasFirstAddendumAssign()) {
          setFirstAddendumAssign(other.getFirstAddendumAssign());
        }
        if (other.hasSecondAddendum()) {
          setSecondAddendum(other.getSecondAddendum());
        }
        if (other.hasSumArea()) {
          mergeSumArea(other.getSumArea());
        }
        this.mergeUnknownFields(other.getUnknownFields());
        return this;
      }
      
      public final boolean isInitialized() {
        if (!hasPresenceStatus()) {
          
          return false;
        }
        if (!hasFirstAddendumAssign()) {
          
          return false;
        }
        if (!hasSecondAddendum()) {
          
          return false;
        }
        if (!hasSumArea()) {
          
          return false;
        }
        if (!getSumArea().isInitialized()) {
          
          return false;
        }
        return true;
      }
      
      public Builder mergeFrom(
          com.google.protobuf.CodedInputStream input,
          com.google.protobuf.ExtensionRegistryLite extensionRegistry)
          throws java.io.IOException {
        com.google.protobuf.UnknownFieldSet.Builder unknownFields =
          com.google.protobuf.UnknownFieldSet.newBuilder(
            this.getUnknownFields());
        while (true) {
          int tag = input.readTag();
          switch (tag) {
            case 0:
              this.setUnknownFields(unknownFields.build());
              onChanged();
              return this;
            default: {
              if (!parseUnknownField(input, unknownFields,
                                     extensionRegistry, tag)) {
                this.setUnknownFields(unknownFields.build());
                onChanged();
                return this;
              }
              break;
            }
            case 8: {
              bitField0_ |= 0x00000001;
              presenceStatus_ = input.readInt32();
              break;
            }
            case 18: {
              bitField0_ |= 0x00000002;
              firstAddendumAssign_ = input.readBytes();
              break;
            }
            case 26: {
              bitField0_ |= 0x00000004;
              secondAddendum_ = input.readBytes();
              break;
            }
            case 34: {
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder subBuilder = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.newBuilder();
              if (hasSumArea()) {
                subBuilder.mergeFrom(getSumArea());
              }
              input.readMessage(subBuilder, extensionRegistry);
              setSumArea(subBuilder.buildPartial());
              break;
            }
          }
        }
      }
      
      private int bitField0_;
      
      // required int32 presenceStatus = 1;
      private int presenceStatus_ ;
      public boolean hasPresenceStatus() {
        return ((bitField0_ & 0x00000001) == 0x00000001);
      }
      public int getPresenceStatus() {
        return presenceStatus_;
      }
      public Builder setPresenceStatus(int value) {
        bitField0_ |= 0x00000001;
        presenceStatus_ = value;
        onChanged();
        return this;
      }
      public Builder clearPresenceStatus() {
        bitField0_ = (bitField0_ & ~0x00000001);
        presenceStatus_ = 0;
        onChanged();
        return this;
      }
      
      // required string firstAddendumAssign = 2;
      private java.lang.Object firstAddendumAssign_ = "";
      public boolean hasFirstAddendumAssign() {
        return ((bitField0_ & 0x00000002) == 0x00000002);
      }
      public String getFirstAddendumAssign() {
        java.lang.Object ref = firstAddendumAssign_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          firstAddendumAssign_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setFirstAddendumAssign(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000002;
        firstAddendumAssign_ = value;
        onChanged();
        return this;
      }
      public Builder clearFirstAddendumAssign() {
        bitField0_ = (bitField0_ & ~0x00000002);
        firstAddendumAssign_ = getDefaultInstance().getFirstAddendumAssign();
        onChanged();
        return this;
      }
      void setFirstAddendumAssign(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000002;
        firstAddendumAssign_ = value;
        onChanged();
      }
      
      // required string secondAddendum = 3;
      private java.lang.Object secondAddendum_ = "";
      public boolean hasSecondAddendum() {
        return ((bitField0_ & 0x00000004) == 0x00000004);
      }
      public String getSecondAddendum() {
        java.lang.Object ref = secondAddendum_;
        if (!(ref instanceof String)) {
          String s = ((com.google.protobuf.ByteString) ref).toStringUtf8();
          secondAddendum_ = s;
          return s;
        } else {
          return (String) ref;
        }
      }
      public Builder setSecondAddendum(String value) {
        if (value == null) {
    throw new NullPointerException();
  }
  bitField0_ |= 0x00000004;
        secondAddendum_ = value;
        onChanged();
        return this;
      }
      public Builder clearSecondAddendum() {
        bitField0_ = (bitField0_ & ~0x00000004);
        secondAddendum_ = getDefaultInstance().getSecondAddendum();
        onChanged();
        return this;
      }
      void setSecondAddendum(com.google.protobuf.ByteString value) {
        bitField0_ |= 0x00000004;
        secondAddendum_ = value;
        onChanged();
      }
      
      // required .RectangleBody sumArea = 4;
      private it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody sumArea_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> sumAreaBuilder_;
      public boolean hasSumArea() {
        return ((bitField0_ & 0x00000008) == 0x00000008);
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody getSumArea() {
        if (sumAreaBuilder_ == null) {
          return sumArea_;
        } else {
          return sumAreaBuilder_.getMessage();
        }
      }
      public Builder setSumArea(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (sumAreaBuilder_ == null) {
          if (value == null) {
            throw new NullPointerException();
          }
          sumArea_ = value;
          onChanged();
        } else {
          sumAreaBuilder_.setMessage(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder setSumArea(
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder builderForValue) {
        if (sumAreaBuilder_ == null) {
          sumArea_ = builderForValue.build();
          onChanged();
        } else {
          sumAreaBuilder_.setMessage(builderForValue.build());
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder mergeSumArea(it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody value) {
        if (sumAreaBuilder_ == null) {
          if (((bitField0_ & 0x00000008) == 0x00000008) &&
              sumArea_ != it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance()) {
            sumArea_ =
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.newBuilder(sumArea_).mergeFrom(value).buildPartial();
          } else {
            sumArea_ = value;
          }
          onChanged();
        } else {
          sumAreaBuilder_.mergeFrom(value);
        }
        bitField0_ |= 0x00000008;
        return this;
      }
      public Builder clearSumArea() {
        if (sumAreaBuilder_ == null) {
          sumArea_ = it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.getDefaultInstance();
          onChanged();
        } else {
          sumAreaBuilder_.clear();
        }
        bitField0_ = (bitField0_ & ~0x00000008);
        return this;
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder getSumAreaBuilder() {
        bitField0_ |= 0x00000008;
        onChanged();
        return getSumAreaFieldBuilder().getBuilder();
      }
      public it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder getSumAreaOrBuilder() {
        if (sumAreaBuilder_ != null) {
          return sumAreaBuilder_.getMessageOrBuilder();
        } else {
          return sumArea_;
        }
      }
      private com.google.protobuf.SingleFieldBuilder<
          it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder> 
          getSumAreaFieldBuilder() {
        if (sumAreaBuilder_ == null) {
          sumAreaBuilder_ = new com.google.protobuf.SingleFieldBuilder<
              it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBody.Builder, it.unipr.aotlab.dmat.core.generated.RectangleWire.RectangleBodyOrBuilder>(
                  sumArea_,
                  getParentForChildren(),
                  isClean());
          sumArea_ = null;
        }
        return sumAreaBuilder_;
      }
      
      // @@protoc_insertion_point(builder_scope:OrderAddAssignBody)
    }
    
    static {
      defaultInstance = new OrderAddAssignBody(true);
      defaultInstance.initFields();
    }
    
    // @@protoc_insertion_point(class_scope:OrderAddAssignBody)
  }
  
  private static com.google.protobuf.Descriptors.Descriptor
    internal_static_OrderAddAssignBody_descriptor;
  private static
    com.google.protobuf.GeneratedMessage.FieldAccessorTable
      internal_static_OrderAddAssignBody_fieldAccessorTable;
  
  public static com.google.protobuf.Descriptors.FileDescriptor
      getDescriptor() {
    return descriptor;
  }
  private static com.google.protobuf.Descriptors.FileDescriptor
      descriptor;
  static {
    java.lang.String[] descriptorData = {
      "\n?home/paolo/uni/dissertation/dmat/proto" +
      "/OrderAddAssignWire.proto\032\023RectangleWire" +
      ".proto\"\202\001\n\022OrderAddAssignBody\022\026\n\016presenc" +
      "eStatus\030\001 \002(\005\022\033\n\023firstAddendumAssign\030\002 \002" +
      "(\t\022\026\n\016secondAddendum\030\003 \002(\t\022\037\n\007sumArea\030\004 " +
      "\002(\0132\016.RectangleBodyB%\n#it.unipr.aotlab.d" +
      "mat.core.generated"
    };
    com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner assigner =
      new com.google.protobuf.Descriptors.FileDescriptor.InternalDescriptorAssigner() {
        public com.google.protobuf.ExtensionRegistry assignDescriptors(
            com.google.protobuf.Descriptors.FileDescriptor root) {
          descriptor = root;
          internal_static_OrderAddAssignBody_descriptor =
            getDescriptor().getMessageTypes().get(0);
          internal_static_OrderAddAssignBody_fieldAccessorTable = new
            com.google.protobuf.GeneratedMessage.FieldAccessorTable(
              internal_static_OrderAddAssignBody_descriptor,
              new java.lang.String[] { "PresenceStatus", "FirstAddendumAssign", "SecondAddendum", "SumArea", },
              it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody.class,
              it.unipr.aotlab.dmat.core.generated.OrderAddAssignWire.OrderAddAssignBody.Builder.class);
          return null;
        }
      };
    com.google.protobuf.Descriptors.FileDescriptor
      .internalBuildGeneratedFileFrom(descriptorData,
        new com.google.protobuf.Descriptors.FileDescriptor[] {
          it.unipr.aotlab.dmat.core.generated.RectangleWire.getDescriptor(),
        }, assigner);
  }
  
  // @@protoc_insertion_point(outer_class_scope)
}
